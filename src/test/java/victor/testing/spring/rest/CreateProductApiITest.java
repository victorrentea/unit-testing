package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

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
  public static final Configuration CONFIGURATION = Configuration.builder()
      .jsonProvider(new JacksonJsonNodeJsonProvider())
      .mappingProvider(new JacksonMappingProvider())
      .build();
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
    // for @JsonFormat or external formal API
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
  void rawTweaked() throws Exception {
    // QA Automators
    String originalJson = IOUtils.toString(getClass()
        .getResource("/data/ProductDto.json"));
    String updatedJson = JsonPath.using(CONFIGURATION)
        .parse(originalJson)
        .set("$.name", "tweak")
        .set("$.supplier[1].name", "subField")
        .json().toString();
    // prefer this, paired with a contract freeze test
    productDto.setName("tweak"); // LOVE

    System.out.println(updatedJson);
    mockMvc.perform(post("/product/create")
            // 1) raw JSON
            .content(updatedJson)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(header().exists("Location")); // response header
  }

  @Test
    // for @Validated, @NotNull..
  void failsForMissingBarcode() throws Exception {
    productDto.setBarcode(null);
    validationFails();
  }

  @Test
    // for @Validated, @NotNull..
  void failsForMissingName() throws Exception {
    productDto.setName(null);
    // In case I detect that the name attribute is annotated with @NotNull
    // I will then generate the test that tries to wipe that
    // field up using Json path
    // or json path: set("$.name", null) > @ParameterizedTest
    // which is gonna be applied on a canonical Json that I keep on my git
    validationFails();
  }

  private void validationFails() throws Exception {
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
    String tenantId = "tenantId";//UUID.randomUUID().toString();
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

    var record =
        productCreatedEventTestListener.blockingReceive(
            ofSeconds(5), // ⚠️ flaky: for how long depends on machine
            r -> r.getT1().value().productId().equals(savedProduct.getId())
                 && r.getT2().equals(tenantId)); // ⚠️tricky: uniquely identify the expected message
    // ⚠️ wrong message sent not matching the criteria timesout the test

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
