package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.SafetyApiWireMock;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import static java.time.Duration.ofSeconds;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static victor.testing.spring.entity.ProductCategory.HOME;

@Transactional
@WithMockUser(roles = "ADMIN") // grant the @Test the ROLE_ADMIN (unless later overridden)
public class ProductApiTest extends IntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  ProductDto productDto = ProductDto.builder()
      .name("Tree")
      .barcode("barcode-safe")
      .supplierCode("S")
      .category(HOME)
      .build();


  @BeforeEach
  final void setupWireMock() {
    SafetyApiWireMock.stubResponse("barcode-safe", "SAFE");

    supplierRepo.save(new Supplier().setCode("S").setActive(true));
  }

  // COPY-paste/inspire/train your ai (context)
  // - ApiTestClient
  // - ProductApiEpicITest
  @Test
  void sendMalformedJson_fails() throws Exception {
    // TODO bad JSON request payload fails
  }

  @Test // test @JsonFormat
  void get_createDateFormat() throws Exception {
    // TODO check format is yyyy-MM-dd (eg 2025-12-25)
  }

  @Test // Test @Validated + @NotNull,@Size...
  void create_failsValidationForMissingBarcode() throws Exception {
    // TODO 1 create product with null barcode return 4xx Client Error containing "barcode" in body
    // TODO 2 load src/test/resources/canonical/CreateProductRequest.json and tweak it using json path
    //   Tip: Canonical.load("CreateProductRequest").set("$.name", "Tree").json().toString()
  }

  @Test // Test @Secured
//  @WithMockUser(roles = ... // downgrade credentials set at class level
  void create_failsForNonAdmin() throws Exception {
    // TODO mockMvc createProduct returns status 403 Forbidden
  }

  @Test
  void create_select_graybox() throws Exception {
    // TODO create via API then select in DB the newly created entity
    //  Tip: KISS Principle: serialize an instance of the DTO to JSON String using 'jackson' field
  }

  @Test
  void create_get_blackbox() throws Exception {
    // TODO create then get the product via API (no DB hit)
    //  + assert 'Location' header is present in the response
  }

  @Test
  void create_sends_message() throws Exception {
    // TODO assert message sent by the tested code
    //  Tip: var event = testListener.blockingReceive(ofSeconds(5));
  }
}
