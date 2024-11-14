package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
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
import victor.testing.spring.service.ProductCreatedEvent;
import victor.testing.tools.Canonical;

import java.util.UUID;

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
public class CreateProductApiITest extends IntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ApiTestDSL api;

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
            // 1) raw JSON - for @JsonFormat or external formal API
            .content("""
                {
                  "name": "Tree",
                  "barcode": "barcode-safe",
                  "supplierCode": "S",
                  "category": "HOME"
                }
                """)

            // 2) serialized JSON (💖💖preferred), paired with ContractFreezeTest
            .content(jackson.writeValueAsString(productDto))

            // 3) load a large JSON from a file💖 and tweak it
            .content(Canonical.load("CreateProductRequest")
                .set("$.name", "COPAC")
                .json()
                .toString())

            .contentType(APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(header().exists("Location")); // response header
  }

  @Test
    // for @Validated, @NotNull..
  void failsForMissingBarcode() throws Exception {
    productDto.setBarcode(null);
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(containsString("barcode")));
  }

  @Test // @Secured
  @WithMockUser(roles = "USER")
    // downgrade credentials set at class level
  void createProductRegularUser_NotAuthorized() throws Exception {
    mockMvc.perform(api.createProductRequest(productDto))
        .andExpect(status().isForbidden());
  }

  @Test
  void create_grayBox() throws Exception {
    String tenantId = UUID.randomUUID().toString();
    MDC.put("tenantId", tenantId);

    // When: API call
    api.createProduct(productDto.setName("Tree"));

    // Then: DB SELECT
    Product savedProduct = productRepo.findAll().get(0);
    assertThat(savedProduct.getName()).isEqualTo("Tree");
    assertThat(savedProduct.getCreatedDate()).isToday();
    assertThat(savedProduct.getCategory()).isEqualTo(productDto.category);
    assertThat(savedProduct.getBarcode()).isEqualTo(productDto.barcode);
    assertThat(savedProduct.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    assertThat(savedProduct.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    assertThat(savedProduct.getSupplier().getCode()).isEqualTo(productDto.supplierCode);

    ConsumerRecord<String, ProductCreatedEvent> record = testListener.blockingReceiveForHeader(
        "tenant-id", tenantId, // ⚠️tricky: uniquely identify the expected message
        ofSeconds(5) // ⚠️ flaky: how long depends on machine
    ); // ⚠️ wrong message sent not matching the criteria times-out the test
    assertThat(record.value().productId()).isEqualTo(savedProduct.getId());
    assertThat(record.value().observedAt()).isCloseTo(now(), byLessThan(5, SECONDS));
  }

  @Test
  void createGet_blackBox() throws Exception {
    long id = api.createProduct(productDto.setName("Tree")); // API call #1

    ProductDto response = api.getProduct(id); // API call #2
    assertThat(response.getName()).isEqualTo("Tree");
    // same assertions as previous test ...
  }
}
