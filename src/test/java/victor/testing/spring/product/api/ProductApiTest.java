package victor.testing.spring.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.api.dto.ProductSearchCriteria;
import victor.testing.spring.product.api.dto.ProductSearchResult;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.tools.HumanReadableTestNames;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static victor.testing.spring.product.domain.ProductCategory.HOME;


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
@EmbeddedKafka(topics = "${input.topic}")
@Transactional
@ActiveProfiles({"db-migration", "wiremock","embedded-kafka"})

@WithMockUser(roles = "ADMIN") // current thread is ROLE_ADMIN
@AutoConfigureMockMvc // ❤️ process the HTTP request in a single thread, without starting a Tomcat => @Transactional works
public class ProductApiTest extends IntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired
  MockMvc mockMvc;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;

  ProductSearchCriteria criteria = new ProductSearchCriteria();
  Long supplierId;
  ProductDto productDto;

  @BeforeEach
  void persistReferenceData() {
    supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
    productDto = new ProductDto("productName", "safe", supplierId, HOME);
  }

  @Test // direct DB access
  void grayBox() throws Exception {
    // API call
    createProductRawJson("Tree");

    // DB SELECT
    Product returnedProduct = productRepo.findAll().get(0);
    assertThat(returnedProduct.getName()).isEqualTo("Tree");
    assertThat(returnedProduct.getCreateDate()).isToday();
    assertThat(returnedProduct.getCategory()).isEqualTo(productDto.category);
    assertThat(returnedProduct.getSupplier().getId()).isEqualTo(productDto.supplierId);
    assertThat(returnedProduct.getSku()).isEqualTo(productDto.sku);
  }

  @Test // (B) only API calls
  void blackBox() throws Exception {
    // API call #1
    createProduct("Tree");

    // API call #2
    List<ProductSearchResult> results = searchProduct(criteria.setName("Tree"));
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getName()).isEqualTo("Tree");
  }

  @Test // (C) sequence of stateful API calls
  void journey() throws Exception {
    // API call #1
    createProduct("Tree");

    // API call #2
    List<ProductSearchResult> results = searchProduct(criteria.setName("Tree"));
    assertThat(results).hasSize(1)
        .first().extracting(ProductSearchResult::getName).isEqualTo("Tree");
    Long productId = results.get(0).getId();

    // API call #3
    ProductDto dto = getProduct(productId);
    assertThat(dto.getCategory()).isEqualTo(productDto.category);
    assertThat(dto.getSupplierId()).isEqualTo(productDto.supplierId);
    assertThat(dto.getSku()).isEqualTo(productDto.sku);
    assertThat(dto.getCreateDate()).isToday();
  }


  // ==================== test-DSL (helper/framework) ======================

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
                                      "    \"sku\": \"safe\"\n" +
                                      "}\n", name, supplierId, HOME);

    mockMvc.perform(post("/product/create")
            .content(createJson)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());
  }
  // #2 ❤️ new DTO => JSON with jackson + Contract Test/Freeze
  void createProduct(String name) throws Exception {
    productDto.setName(name)
        .setSupplierId(supplierId)
        .setCategory(HOME)
        .setSku("safe");

    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON)) // can be set as default
        .andExpect(status().is2xxSuccessful());
  }


  List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) throws Exception {
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

  ProductDto getProduct(long productId) throws Exception {
    String responseJson = mockMvc.perform(get("/product/{id}", productId))
        .andExpect(status().is2xxSuccessful())
        .andReturn().getResponse().getContentAsString();
    return jackson.readValue(responseJson, ProductDto.class);
  }

  // ==================== More stuff you can test with MockMvc ======================

  @Test
  void cannotCreateProductWithNullName() throws Exception {
    productDto.setName(null); // triggers @Validated on controller method

    MvcResult mvcResult = mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON))
        // see the @RestControllerAdvice
        .andExpect(status().is4xxClientError())
        .andReturn();
    assertThat(mvcResult.getResponse().getContentAsString()).contains("name");
  }

  @Test
  void createProduct_returnsHeader() throws Exception {
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON))
        .andExpect(header().string("Location", "http://created-uri"))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(roles = "USER") // resets the credentials set at the class level
  void createProductByNonAdmin_NotAuthorized() throws Exception {
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(productDto))
            .contentType(APPLICATION_JSON)
        )
        .andExpect(status().isForbidden());
  }

}
