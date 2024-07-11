package victor.testing.spring.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import victor.testing.spring.BaseIntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.api.dto.ProductSearchCriteria;
import victor.testing.spring.api.dto.ProductSearchResult;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.tools.HumanReadableTestNames;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static victor.testing.spring.domain.ProductCategory.HOME;


@DisplayNameGeneration(HumanReadableTestNames.class)

@AutoConfigureWireMock(port = 0) // Start a HTTP server on a random port serving canned JSONs
@EmbeddedKafka(topics = "${input.topic}") // start up an in-mem Kafka

@WithMockUser(roles = "ADMIN") // grant the current thread the 'ROLE_ADMIN'
@AutoConfigureMockMvc // emulates HTTP requests in the current thread, without starting a Tomcat
// I want to keep on 1 thread a test so @Transactional works

//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // alterntively, to boot up a Tomcat
// you will call apis using restTemplate
public class ProductApiTest extends BaseIntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

//  mockbean kafka

  // Dtos with 'default' values (ObjectMother style)
  ProductSearchCriteria criteria = new ProductSearchCriteria();
  ProductDto productDto = new ProductDto(
      "Tree",
      "barcode-safe",
      "S",
      HOME);

//
//  intai un test cu un """ JSON
//"""
//
//  apoi cu serializare jackson
//
//
//      apoi @Validated
//
//      apoi authorization
//
//  apoi gray  si black si journey

  @BeforeEach
  void persistReferenceData() {
//    supplierRepo.save(new Supplier().setCode("S").setActive(true));
  }

  @Test
  void grayBox() throws Exception {
    // repo.save(..); Given

    // API call (when)
    // TODO vrentea 11.07.2024:
    /*long id = */createProductApi(productDto.setName("Tree"));

    // direct DB SELECT (then)
    Product returnedProduct = productRepo.findAll().get(0);
    assertThat(returnedProduct.getName()).isEqualTo("Tree");
    assertThat(returnedProduct.getCreatedDate()).isToday();
    assertThat(returnedProduct.getCategory()).isEqualTo(productDto.category);
    assertThat(returnedProduct.getBarcode()).isEqualTo(productDto.barcode);
  }

  @Test
  void blackBox() throws Exception {
    // API call #1
    createProductApi(productDto.setName("Tree"));

    // API call #2
    // TODO vrentea 11.07.2024: GET by ID
    List<ProductSearchResult> results = searchProductApi(criteria.setName("Tree"));
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).isEqualTo("Tree");
  }

  @Test // probably better to impement as separate @Test in a stateful test class
  void flow() throws Exception {
    // API call #1
    createProductApi(productDto.setName("Tree"));

    // API call #2
    List<ProductSearchResult> results = searchProductApi(criteria.setName("Tree"));
    assertThat(results).hasSize(1);
    assertThat(results.stream().map(ProductSearchResult::getName)).containsExactly("Tree");
    Long productId = results.get(0).getId();

    // API call #3
    ProductDto dto = getProductApi(productId);
    assertThat(dto.getCategory()).isEqualTo(productDto.category);
    assertThat(dto.getBarcode()).isEqualTo(productDto.getBarcode());
    assertThat(dto.getCreatedOn()).isToday();
  }


  // ==================== test-DSL (helper/framework) ======================

  private void createProductApi(ProductDto request) throws Exception {
    mockMvc.perform(createProductRequest(request)) // can be set as default
        .andExpect(status().is2xxSuccessful())
        .andExpect(header().exists("Location"));
  }
  private MockHttpServletRequestBuilder createProductRequest(ProductDto request) throws JsonProcessingException {
    return post("/product/create")
        .content(jackson.writeValueAsString(request))
        .contentType(APPLICATION_JSON);
  }

  private List<ProductSearchResult> searchProductApi(ProductSearchCriteria criteria) throws Exception {
    String responseJson = mockMvc.perform(post("/product/search")
            .content(jackson.writeValueAsString(criteria))
            .contentType(APPLICATION_JSON)
        )
        .andExpect(status().is2xxSuccessful())
        .andReturn()
        .getResponse()
        .getContentAsString();
    return List.of(jackson.readValue(responseJson, ProductSearchResult[].class)); // trick to unmarshall a collection<obj>
  }

  private ProductDto getProductApi(long productId) throws Exception {
    String responseJson = mockMvc.perform(get("/product/{id}", productId))
        .andExpect(status().is2xxSuccessful())
        .andReturn().getResponse().getContentAsString();
    return jackson.readValue(responseJson, ProductDto.class);
  }

  // ====== test @Validated

//  @Test
//  void createProduct_failsForMissingName() throws Exception {
//    var errorBody = failsValidationWithMessage(productDto.setName(null));
//    assertThat(errorBody).contains("name");
//  }

  @Test
  void createProduct_failsForMissingBarcode() throws Exception {
    productDto.setBarcode(null);
    var errorBody = mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto)) // serializing the DTO as a JSON String

        // explicit JSON:
//            .content("""
//                {
//                  "name": "Tree",
//                  "supplierCode": "S",
//                  "category": "HOME"
//                }
//                """)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andReturn()
        .getResponse()
        .getContentAsString();
//    var errorBody = mockMvc.perform(createProductRequest(productDto)) // 💖
//        .andExpect(status().is4xxClientError()) // 400 BAD REQUEST thanks to @RestControllerAdvice
//        .andReturn()
//        .getResponse()
//        .getContentAsString();
    assertThat(errorBody).contains("barcode");
  }


  @Test
  void checkJSONDateFormatting() throws Exception {
    Supplier supplier = supplierRepo.findByCode("S").get();
    Long newId = productRepo.save(new Product().setSupplier(supplier)).getId();

    String json = mockMvc.perform(get("/product/" + newId))
        .andExpect(status().is2xxSuccessful())
        .andReturn()
        .getResponse()
        .getContentAsString();

    assertThat(json).contains("createdOn");
    assertThat(json).contains("11/07/2024");
  }
  // ====== test authorization

  @Test
  @WithMockUser(roles = "USER") // resets the credentials set at the class level
  void createProductRegularUser_NotAuthorized() throws Exception {
    mockMvc.perform(createProductRequest(productDto))
        .andExpect(status().isForbidden());
  }

}
