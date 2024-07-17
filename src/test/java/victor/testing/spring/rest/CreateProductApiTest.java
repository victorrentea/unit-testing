package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;
import victor.testing.spring.service.ProductCreatedEvent;

import java.util.List;

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

@Transactional
@WithMockUser(roles = "ADMIN") // grant the @Test the ROLE_ADMIN (unless later overridden)
public class CreateProductApiTest extends IntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  ProductSearchCriteria criteria = new ProductSearchCriteria();

  ProductDto productDto = new ProductDto(
      "Tree",
      "barcode-safe",
      "S",
      HOME);

  @BeforeEach
  void setup() {
    supplierRepo.save(new Supplier().setCode("S").setActive(true));
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
  ApiTestDSL dsl;

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
    assertThat(returnedProduct.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    assertThat(returnedProduct.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    assertThat(returnedProduct.getSupplier().getCode()).isEqualTo(productDto.supplierCode);
    var message = productCreatedEventTestListener.blockingReceive(ofSeconds(5)); // potentially flaky
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

}
