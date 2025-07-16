package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.SafetyApiWireMock;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

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

  @Autowired
  private RestTemplate restTemplate;
  private ProductDto dto = ProductDto.builder()
      .name("Tree2")
      .barcode("barcode-safe")
      .supplierCode("S")
      .category(HOME)
      .build();
  private Supplier supplier;


  @BeforeEach
  final void setupWireMock() {
    SafetyApiWireMock.stubResponse("barcode-safe", "SAFE");

    supplier = supplierRepo.save(new Supplier().setCode("S").setActive(true));
  }

  // COPY-paste/inspire/train your ai (context)
  // - ApiTestClient
  // - ProductApiEpicITest
  @Test
  void sendMalformedJson_fails() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/product/create")
            .content("""
                {
                  "name": "Tree",
                  "barcode": "barcode-safe",
                  "supplierCode": "S",
                  "category": "HOME"
                
                """)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test // test @JsonFormat
  // TODO check format is yyyy-MM-dd (eg 2025-12-25)
  void get_createDateFormat() throws Exception {
    // black-box: create tot prin API call
    // gray-box: repo.save
    Long productId = productRepo.save(new Product().setSupplier(supplier)).getId();
    // white-box: @MockBean ProductService/ @Mock

    // as vrea sa verific ca in jsonResponse pe calea $.createDate am stringu "2025-07-16"
    mockMvc.perform(MockMvcRequestBuilders.get(("/product/" + productId)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.createdDate")
            .value("2025-07-16"));
  }

//            .content(Canonical.load("CreateProductRequest").set("$.barcode", null).json().toString())
  @Test
  void create_failsValidationForMissingBarcode() throws Exception {
    createFailsValidationFor(dto.withBarcode(null));
  }
  @Test
  void create_failsValidationForMissingName() throws Exception {
    createFailsValidationFor(dto.withName(null));
  }
  @Test
  void create_failsValidationForEmptyName() throws Exception {
    createFailsValidationFor(dto.withName(""));
  }
  @Test
  void create_failsValidationForSpaceName() throws Exception {
    createFailsValidationFor(dto.withName(" "));
  }

  private void createFailsValidationFor(ProductDto dto) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/product/create")
            .content(jackson.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }

  @Test // Test @Secured
  @WithMockUser(roles = "FRAER")
  void create_failsForNonAdmin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/product/create")
            .content(jackson.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
//    assertThat(productRepo.findByName("Tree2")).isNull();
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
            .content(jackson.writeValueAsString(dto))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    assertThat(productRepo.findByName("Tree2")).isNotNull();
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
