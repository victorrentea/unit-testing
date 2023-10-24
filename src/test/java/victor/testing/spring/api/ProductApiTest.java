package victor.testing.spring.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
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


@DisplayNameGeneration(HumanReadableTestNames.class) // makes test names look nice

@SpringBootTest
//@DataJpaTest //slice tests

@AutoConfigureWireMock(port = 0) // Start a HTTP server on a random port serving canned JSONs
@EmbeddedKafka(topics = "${input.topic}") // start up an in-mem Kafka
@Transactional // ROLLBACK after each @Test
@ActiveProfiles({"db-migration", "wiremock","embedded-kafka"})

@WithMockUser(roles = "ADMIN") // grant the current thread the 'ROLE_ADMIN'
@AutoConfigureMockMvc // process HTTP requests in current thread, without a Tomcat
public class ProductApiTest extends IntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  // Dtos with 'default' values (ObjectMother style)
  ProductSearchCriteria criteria = new ProductSearchCriteria();
  ProductDto productDto;
  Long supplierId;

  @BeforeEach
  void persistReferenceData() {
    supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
    productDto = new ProductDto("Tree", "upc-safe", supplierId, HOME);
  }

  @Test
  void grayBox() throws Exception {
    // REST API call - black
    createProduct(productDto.setName("Tree"));

    // DB SELECT - white
    Product returnedProduct = productRepo.findAll().get(0);
    assertThat(returnedProduct.getName()).isEqualTo("Tree");
    assertThat(returnedProduct.getCreatedDate()).isToday();
    assertThat(returnedProduct.getCategory()).isEqualTo(productDto.category);
    assertThat(returnedProduct.getUpc()).isEqualTo(productDto.upc);
  }

  @Test
  void blackBox() throws Exception {
    // API call #1
    createProduct(productDto.setName("Tree"));

    // API call #2
    List<ProductSearchResult> results = searchProduct(criteria.setName("Tree"));
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).isEqualTo("Tree");
  }

  @Test
  void userJourney() throws Exception {
    // API call #1
    createProduct(productDto.setName("Tree"));

    // API call #2
    List<ProductSearchResult> results = searchProduct(criteria.setName("Tree"));
    assertThat(results).hasSize(1);
    assertThat(results.stream().map(ProductSearchResult::getName)).containsExactly("Tree");
    Long productId = results.get(0).getId();

    // API call #3
    ProductDto dto = getProduct(productId);
    assertThat(dto.getCategory()).isEqualTo(productDto.category);
    assertThat(dto.getUpc()).isEqualTo(productDto.getUpc());
    assertThat(dto.getCreatedDate()).isToday();
  }


  // ==================== test-DSL (helper/framework) ======================

  private void createProduct(ProductDto request) throws Exception {
    mockMvc.perform(createProductRequest(request)) // can be set as default
        .andExpect(status().is2xxSuccessful())
        .andExpect(header().exists("Location"));
  }
  private MockHttpServletRequestBuilder createProductRequest(ProductDto request) throws JsonProcessingException {
    return post("/product/create")
        .content(jackson.writeValueAsString(request))
        .contentType(APPLICATION_JSON);
  }

  private List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) throws Exception {
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

  private ProductDto getProduct(long productId) throws Exception {
    String responseJson = mockMvc.perform(get("/product/{id}", productId))
        .andExpect(status().is2xxSuccessful())
        .andReturn().getResponse().getContentAsString();
    return jackson.readValue(responseJson, ProductDto.class);
  }

  // ====== test @Validated

  @Test
  void createProduct_failsForMissingName() throws Exception {
    createProduct_failsValidation(productDto.setName(null), "name");
  }

  @Test
  void createProduct_failsForMissingUPC() throws Exception {
    createProduct_failsValidation(productDto.setUpc(null), "upc");
  }

  private void createProduct_failsValidation(ProductDto request, String fieldName) throws Exception {
    String errorBody = mockMvc.perform(createProductRequest(request))
        .andExpect(status().is4xxClientError()) // thanks to @RestControllerAdvice
        .andReturn()
        .getResponse()
        .getContentAsString();
    assertThat(errorBody).contains(fieldName); // reports the field name in error
  }

  // ====== test authorization

  @Test
  @WithMockUser(roles = "USER") // resets the credentials set at the class level
  void createProductRegularUser_NotAuthorized() throws Exception {
    mockMvc.perform(createProductRequest(productDto))
        .andExpect(status().isForbidden());
  }

}
