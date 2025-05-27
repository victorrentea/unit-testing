package victor.testing.spring.rest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.service.ProductCreatedEvent;

import java.util.UUID;

import static java.time.Duration.ofSeconds;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static victor.testing.spring.entity.ProductCategory.HOME;

@Transactional // periculos!
@WithMockUser(roles = "ADMIN") // default role for all tests
public class CreateProductITest extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  ApiTestDSL api;

  ProductDto productDto = ProductDto.builder()
      .name("Tree")
      .barcode("barcode-safe")
      .supplierCode("S")
      .category(HOME)
      .build();

  @BeforeEach
  final void setupWireMock() {
    SafetyApi.stubResponse("barcode-safe", "SAFE");
  }

  @Test
  void raw() throws Exception {
    var id = api.createProduct(productDto.withName("Tree"));

    // APEL IN BAZA = GrayBox TEST
//    Product savedProduct = productRepo.findById(id).orElseThrow();
//    assertThat(savedProduct.getName()).isEqualTo("Tree");
    
    // BLACK BOX
    var dtoLaGet = api.getProduct(id);
    assertThat(dtoLaGet.name()).isEqualTo("Tree");
  }

  @Test
  void validationFails_forMissingBarcode() throws Exception {
    api.expectValidationFails(productDto.withBarcode(null), "barcode");
  }

  @Test
  @WithMockUser(roles = "USER") // downgrade credentials set at class level
  void isNotAuthorized_forRegularUser() throws Exception {
    mockMvc.perform(api.createProductRequest(productDto))
        .andExpect(status().isForbidden());
  }

  @Test
  void create_grayBox() throws Exception {
    String tenantId = UUID.randomUUID().toString();
    MDC.put("tenantId", tenantId);

    // When: API call
    api.createProduct(productDto.withName("Tree"));

    // Then: DB SELECT
    Product savedProduct = productRepo.findAll().get(0);
    assertThat(savedProduct.getName()).isEqualTo("Tree");
    assertThat(savedProduct.getCreatedDate()).isToday();
    assertThat(savedProduct.getCategory()).isEqualTo(productDto.category());
    assertThat(savedProduct.getBarcode()).isEqualTo(productDto.barcode());
    assertThat(savedProduct.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    assertThat(savedProduct.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    assertThat(savedProduct.getSupplier().getCode()).isEqualTo(productDto.supplierCode());

    ConsumerRecord<String, ProductCreatedEvent> record = testListener.blockingReceiveForHeader(
        "tenant-id", tenantId, // ⚠️trick: uniquely identify the expected message
        ofSeconds(5) // ⚠️ flaky: maxWaitingTime is that of the weakest dev machine
    ); // ⚠️ wrong message sent not matching the criteria times-out the test
    assertThat(record.value().productId()).isEqualTo(savedProduct.getId());
    assertThat(record.value().observedAt()).isCloseTo(now(), byLessThan(5, SECONDS));
  }

  @Test
  void createGet_blackBox() throws Exception {
    long id = api.createProduct(productDto.withName("Tree")); // API call #1

    ProductDto response = api.getProduct(id); // API call #2
    assertThat(response.name()).isEqualTo("Tree");
    // same assertions as previous test ...
  }
}
