package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;
import victor.testing.spring.service.ProductCreatedEvent;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;


@SpringBootTest
@ActiveProfiles("test") // pick up properties from application-test.properties
@AutoConfigureWireMock(port = 0) // Start a HTTP server on a random port serving canned JSONs
@Transactional // ROLLBACK after each @Test
@EmbeddedKafka(topics = PRODUCT_CREATED_TOPIC)
@WithMockUser(roles = "ADMIN") // grant the @Test the ROLE_ADMIN (unless later overridden)
@AutoConfigureMockMvc // emulates a HTTP call to my system
public class CreateProductApiTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  KafkaTestConsumer testConsumer;


  ProductSearchCriteria criteria = new ProductSearchCriteria();

  ProductDto productDto = new ProductDto(
      "Tree",
      "barcode-safe",
      "S",
      HOME);

  @BeforeEach
  void setup() {
    supplierRepo.save(new Supplier().setCode("S").setActive(true));
    testConsumer.reset();
  }

  @Test
  void raw() throws Exception {
    mockMvc.perform(post("/product/create")
            // 1) raw JSON
            .content("""
                {
                  "name": "Tree",
                  "barcode": "barcode-safe",
                  "supplierCode": "S",
                  "category": "HOME"
                }
                """)
            // 2) serialized JSON (preferred), paired with ContractFreezeTest
            // .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(header().exists("Location")); // response header
  }

  @Test
    // @Validated
  void failsForMissingBarcode() throws Exception {
    productDto.setBarcode(null);
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(containsString("barcode")));
  }

  @Autowired
  ProductApiTestDSL dsl;

  @Test // @Secured
  @WithMockUser(roles = "USER")
    // resets credentials set at class level
  void createProductRegularUser_NotAuthorized() throws Exception {
    mockMvc.perform(dsl.createProductRequest(productDto))
        .andExpect(status().isForbidden());
  }

  @Test
  void grayBox_create() throws Exception {
    // repo.save(..); Given

    // API call (when)
    dsl.createProductApi(productDto.setName("Tree"));

    // direct DB SELECT (then)
    Product returnedProduct = productRepo.findAll().get(0);
    assertThat(returnedProduct.getName()).isEqualTo("Tree");
    assertThat(returnedProduct.getCreatedDate()).isToday();
    assertThat(returnedProduct.getCategory()).isEqualTo(productDto.category);
    assertThat(returnedProduct.getBarcode()).isEqualTo(productDto.barcode);
    assertThat(returnedProduct.getSupplier().getCode()).isEqualTo(productDto.supplierCode);
    // ⚠️FLAKY: can fail for timeout exceeded
    ConsumerRecord<String, ProductCreatedEvent> message = testConsumer.blockingReceive(ofSeconds(5)); // blocking
    assertThat(message.value().productId()).isEqualTo(returnedProduct.getId());
    assertThat(message.value().observedAt()).isCloseTo(now(), byLessThan(5, SECONDS));
  }

  @Test
  void blackBox_createSearch() throws Exception {
    // API call #1
    dsl.createProductApi(productDto.setName("Tree"));

    // API call #2
    List<ProductSearchResult> results = dsl.searchProductApi(criteria.setName("Tree"));
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).isEqualTo("Tree");
  }

  @Test
    // perhaps better as sequenced @Test like StatefulFlowTest.java
  void flow() throws Exception {
    // API call #1
    dsl.createProductApi(productDto.setName("Tree"));

    // API call #2
    List<ProductSearchResult> searchResponse = dsl.searchProductApi(criteria.setName("Tree"));
    assertThat(searchResponse).map(ProductSearchResult::getName).containsExactly("Tree");
    Long productId = searchResponse.get(0).getId();

    // API call #3
    ProductDto getResponse = dsl.getProductApi(productId);
    assertThat(getResponse)
        .returns(productDto.getName(), ProductDto::getName)
        .returns(productDto.getBarcode(), ProductDto::getBarcode)
        .returns(productDto.getCategory(), ProductDto::getCategory);
  }

  @TestConfiguration
  public static class KafkaTestConfig {
    @Bean
    KafkaTestConsumer testConsumer() {
      return new KafkaTestConsumer();
    }
  }

  @Slf4j
  private static class KafkaTestConsumer {
    private CompletableFuture<ConsumerRecord<String, ProductCreatedEvent>> receivedRecord = new CompletableFuture<>();

    @KafkaListener(topics = PRODUCT_CREATED_TOPIC)
    void receive(ConsumerRecord<String, ProductCreatedEvent> consumerRecord) {
      log.info("received payload='{}'", consumerRecord.toString());
      receivedRecord.complete(consumerRecord);
    }

    public ConsumerRecord<String, ProductCreatedEvent> blockingReceive(Duration timeout) throws ExecutionException, InterruptedException {
      return receivedRecord.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS).get();
    }

    public void reset() {
      receivedRecord = new CompletableFuture<>();
    }
  }
}
