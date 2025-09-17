package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.SafetyApiWireMock;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.tools.Canonical;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static victor.testing.spring.entity.ProductCategory.HOME;

// grant the @Test the ROLE_ADMIN (unless later overridden)
@Transactional
@WithMockUser(roles = "ADMIN")
class ProductApi0Test extends IntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());

  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  @BeforeEach
  final void init() {
    // wiremocks stubs for ext api calls
    SafetyApiWireMock.stubResponse("barcode-safe", "SAFE");

    // ref data
    supplierRepo.save(new Supplier().setCode("S").setActive(true));
  }
  ProductDto dto = ProductDto.builder()
      .name("Tree")
      .barcode("barcode-safe")
      .supplierCode("S")
      .category(HOME)
      .build();

  // Hint: Inspire from ApiTestClient and ProductApiEpicITest
  private @NonNull ResultActions createProduct(ProductDto dto) throws Exception {
    return mockMvc.perform(post("/product/create")
        .content(jackson.writeValueAsString(dto))
        .contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void create_select_graybox() throws Exception {
    createProduct(dto)
        .andExpect(MockMvcResultMatchers.status().isCreated());

    Product product = productRepo.findByName("Tree");
    assertThat(product).isNotNull()
        .returns("Tree", Product::getName)
        .returns("barcode-safe", Product::getBarcode)
        .returns("S", p -> p.getSupplier().getCode());
  }

  @Test
  @WithMockUser(roles = "USER")
  void create_failsForNonAdmin() throws Exception {
    createProduct(dto)
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test // for @Validated of @NotNull, @NotBlank, @Size...
  void create_failsValidationForMissingBarcode() throws Exception {
    ProductDto invalidDto = dto.withBarcode(null);
    createProduct(invalidDto)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(containsString("barcode")));
  }
  @Test // for @Validated of @NotNull, @NotBlank, @Size...
  void create_failsValidationTweakingALargeJson() throws Exception {
    String json = Canonical.load("CreateProductRequest.json")
        .set("$.supplier[0].name", null)
        .set("$.barcode", null)
        .set("$.name", null)
        .json().toString();
    mockMvc.perform(post("/product/create")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(containsString("barcode")))
        .andExpect(MockMvcResultMatchers.content().string(containsString("name")))
    ;
  }

  @Test
  void create_sendBadJson_fails() throws Exception {
    // TODO bad JSON request payload => 500 Internal Server Error
  }

  @Test
    // test @JsonFormat
  void get_createDateFormat() throws Exception { // TODO remove
    // TODO check date format is yyyy-MM-dd (eg 2025-12-25)
  }

  @Test
  void create_get_blackbox() throws Exception { //TODO tweak #1
    // TODO create then get the product via API (without accessing the DB)
    //  Tip: extract 'Location' using .andExpect(..).andReturn().getResponse().getHeader(..)
    // TODO GET /product/{id} => assert fields in response DTO
  }

  @Test
  void create_sends_message() throws Exception { // TODO REMOVE
    // TODO assert message is sent with testListener.blockingReceive(ofSeconds(5));
  }

  // TODO test that created product has createdDate today.
}
