package victor.testing.spring.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;
import victor.testing.tools.HumanReadableTestNames;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static victor.testing.spring.domain.ProductCategory.HOME;


// WHY to test a controller?
// - Logic ==> @Autowire the controller and call its methods!
// - Contract: url, POST.., DTO structure


// - HTTP Status Code, @RestControllerAdvice ==> does the code matter?
// - @Validated on params of @RestController methods
// - Security: @Secured, @PreAuthorized, mvcMatcher("/admin/**), SecurityContextHolder usage

@DisplayNameGeneration(HumanReadableTestNames.class) // makes test names look nice


/**
 * <li> Connects to a production-like DB in a Docker image
 * <li> Runs the flyway migration scripts against the empty DB
 * <li> Uses WireMock to stub the JSON responses from third party APIs
 * <li> Starts one @Transaction / @Test
 * <li> Fills some 'static' data in the database (Supplier)
 * <li> pre-autorizes the request as ROLE_ADMIN by default
 * <li> Emulates a JSON request against my API and checks the JSON response
 * <li> At the end of each test leaves the DB clean (by auto-rollback of @Transactional)
 */
@SpringBootTest

@AutoConfigureWireMock(port = 0)
//@Testcontainers
@Transactional // needs the test and prod to share the thread
@ActiveProfiles({"db-mem", "wiremock"})

@WithMockUser(roles = "ADMIN") // current thread is ROLE_ADMIN
@AutoConfigureMockMvc // use these!!
// ❤️ emulates HTTP request without starting a Tomcat => @Transactional works, as the whole test shares 1 single thread
public class ProductApiTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private SupplierRepo supplierRepo;
  @Autowired
  private ProductRepo productRepo;

  protected Long supplierId;
  protected ProductDto productDto;

  @BeforeEach
  public void persistReferenceData() {
    supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
    productDto = new ProductDto("productName", "safebar", supplierId, HOME);
  }

  @Test
  public void whiteBox() throws Exception {
    createProduct("Tree");

    // (A) white box = direct DB access
    Product returnedProduct = productRepo.findAll().get(0);
    assertThat(returnedProduct.getName()).isEqualTo("Tree");
    assertThat(returnedProduct.getCreateDate()).isToday();
    assertThat(returnedProduct.getCategory()).isEqualTo(productDto.category);
    assertThat(returnedProduct.getSupplier().getId()).isEqualTo(productDto.supplierId);
    assertThat(returnedProduct.getBarcode()).isEqualTo(productDto.barcode);
  }

  @Test // only targets the REST API < the most robust tests, in practice very hard to write
  public void blackBoxFlow_userJourneyTest() throws Exception {
    createProduct("Tree"); // call#1

    // (B) black box = only API calls; more decoupled
    List<ProductSearchResult> results = searchProduct(criteria().setName("Tree")); // call#2
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).isEqualTo("Tree");
    Long productId = results.get(0).getId();

    ProductDto returnedProduct = getProduct(productId); // call#3
//    assertThat(returnedProduct).ignorifFIelds("$.microcontroller.pins.[].id")
    assertThat(returnedProduct.getCategory()).isEqualTo(productDto.category);
    assertThat(returnedProduct.getSupplierId()).isEqualTo(productDto.supplierId);
    assertThat(returnedProduct.getBarcode()).isEqualTo(productDto.barcode);
    assertThat(returnedProduct.getCreateDate()).isToday();
  }


  // ==================== test-DSL (helper/framework) ======================

  private static ProductSearchCriteria criteria() {
    return new ProductSearchCriteria();
  }

  // #1 RAW JSON in test
  // - cumbersome
  // + breaks on Dto structure change:
  //    * Prevent accidental changes to my API ==> OpenAPIFreezeTest
  //    * Keep consumer-provider in sync👌 ==> Pact / Spring Cloud Contract Tests
  void createProductRawJson(String name) throws Exception {
    // language=json
    String createJson = String.format("{\n" +
                                      "    \"name\": \"%s\",\n" +
                                      "    \"supplierId\": \"%d\",\n" +
                                      "    \"category\" : \"%s\",\n" +
                                      "    \"barcode\": \"safebar\",\n" +
                                      "    \"meetoo\": 1\n" +
                                      "}\n", name, supplierId, HOME);
// instead of JSON in a string, use victor.testing.spring.web.OpenAPIFreezeTest
    mockMvc.perform(post("/product/create")
            .content(createJson)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
    ;
  }

  // #2 ❤️ ❤️ ❤️ new DTO => JSON with jackson + Contract Test/Freeze
  void createProduct(String name) throws Exception {
    productDto.setName(name)
        .setSupplierId(supplierId)
        .setCategory(HOME)
        .setBarcode("safebar");

    performCreateProduct() // can be set as default
        .andExpect(status().is2xxSuccessful());
  }

  @NotNull
  private ResultActions performCreateProduct() throws Exception {
    return mockMvc.perform(post("/product/create")
        .content(jackson.writeValueAsString(productDto))
        .contentType(APPLICATION_JSON));
  }

  @Test
  void createFailsForProductWithNoName() throws Exception {
    productDto.setName(null);

    performCreateProduct().andExpect(status().is4xxClientError());
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

  // ==================== More stuff you can test with MockMvc ======================

  @Test
  void createProduct_returnsHeader() throws Exception {
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON))
        .andExpect(header().string("Location", "http://created-uri"))
        .andExpect(status().isCreated());
  }

  // test authorization
  @Test
  @WithMockUser(roles = "USER") // resets the credentials set at the class level
  public void createProductByNonAdmin_NotAuthorized() throws Exception {
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON)
        )
        .andExpect(status().isForbidden());
  }

  @Test
  public void cannotCreateProductWithNullName() throws Exception {
    productDto.setName(null); // triggers @Validated on controller method

    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON))
        .andExpect(status().is4xxClientError()); // see the @RestControllerAdvice
  }

}
