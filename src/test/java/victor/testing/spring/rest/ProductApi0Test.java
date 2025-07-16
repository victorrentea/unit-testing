package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.SafetyApiWireMock;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.tools.Canonical;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.entity.ProductCategory.HOME;

@Transactional
@WithMockUser(roles = "ADMIN") // grant the @Test the ROLE_ADMIN (unless later overridden)
public class ProductApi0Test extends IntegrationTest {
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
  @Autowired
  private RestTemplate restTemplate;


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
//    restTemplate.postForEntity()// rau pt ca schimba threadul si nu mai merge @Transactional nici @WithMockUser
    mockMvc.perform(MockMvcRequestBuilders.post("/product/create")
        // #1 json brut ca string
//        .content("""
//                {
//                  "name": "Tree",
//                  "barcode": "barcode-safe",
//                  "supplierCode": "S",
//                  "category": "HOME"
//                }
//                """)
        // #2 incarc un fisier un json mare DE CARE NU-MI PASA si editez bucati din el
//        .content(Canonical.load("CreateProductRequest")
//            .set("$.name", "Tree2")
//            .json()
//            .toString())
        // #3 jsonific o instanta de DTO
            .content(jackson.writeValueAsString(ProductDto.builder()
                    .name("Tree2")
                    .barcode("barcode-safe")
                    .supplierCode("S")
                    .category(HOME)
                .build()))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    assertThat(productRepo.findByName("Tree2")).isNotNull();
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
