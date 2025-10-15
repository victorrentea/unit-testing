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
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.SafetyApiWireMock;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.tools.Canonical;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static victor.testing.spring.entity.ProductCategory.HOME;

@Transactional
@WithMockUser(roles = "ADMIN") // grant the @Test the ROLE_ADMIN (unless later overridden)
public class ProductApi0Test extends IntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  public static final String BARCODE = "barcode-safe";

  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  @BeforeEach
  final void init() {
    SafetyApiWireMock.stubResponse(BARCODE, "SAFE");

    supplierRepo.save(new Supplier().setCode("S").setActive(true));// date de referinta
  }

  ProductDto dto = ProductDto.builder()
      .name("Tree")
      .barcode(BARCODE)
      .supplierCode("S")
      .category(HOME)
      .build();
  // Hint: Inspire from ApiTestClient and ProductApiEpicITest
  @Test
  void create_select_graybox() throws Exception {
    // REST API call catre app mea BLACKBOX
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());

    Product product = productRepo.findByName("Tree"); // direct DB acces face acest test "GRAYBOX"
    assertThat(product.getCreatedDate()).isToday();
    assertThat(product.getBarcode()).isEqualTo(BARCODE);
  }

  @Test
  @WithMockUser(roles = "USER")
  void create_failsForNonAdmin() throws Exception {
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test // for @Validated of @NotNull, @NotBlank, @Size...
  void create_failsValidationForMissingBarcode() throws Exception {
    String responseBody = mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(dto.withBarcode(null).withName("")))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();
    assertThat(responseBody)
        .containsIgnoringCase("barcode")
        .containsIgnoringCase("name");
    // TODO 3(STAR) adjust a JSON loaded from /src/test/resource without working with a DTO instance:
    //  Canonical.load("CreateProductRequest.json").set("$.name", null).json().toString()
    //  loads src/test/resources/canonical/CreateProductRequest.json
  }

  @Test
  void create_candTrecPrinMineMarfareDeDateDeCareNuMiPasa() throws Exception {
    String json = Canonical.load("CreateProductRequest.json").set("$.name", null).json().toString();
    mockMvc.perform(post("/product/create")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_sendBadJson_fails() throws Exception {
    mockMvc.perform(post("/product/create")
            .content("{")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void get_createDateFormat() throws Exception {
    // TODO check date format is yyyy-MM-dd (eg 2025-12-25) using JSON Path --> ask AI
    //  This test should fail if I change the pattern in @JsonFormat(pattern = "yyyy-MM-dd") in ProductDto
    // ia din /

    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful()); // TODO sa fac si io ca sf. Uncle Bob sa ascund apelurilea astea in niste Testing Support Code

    mockMvc.perform(get("/product/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.createdDate").value("2025-10-15")); // TODO reparati voi maine :(
  }

  @Test
  void create_get_blackbox() throws Exception {
    // TODO create then get the product via API (without accessing the DB)
    //  Tip: extract 'Location' using .andExpect(..).andReturn().getResponse().getHeader(..)
    // TODO GET /product/{id} => assert fields in response DTO

    var mvcResult = mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andReturn();
    String location = mvcResult.getResponse().getHeader("Location");
    assertThat(location).isNotBlank();
    String[] parts = location.split("/");
    String id = parts[parts.length - 1];
    mockMvc.perform(get("/product/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Tree"))
        .andExpect(jsonPath("$.barcode").value(BARCODE))
        .andExpect(jsonPath("$.supplierCode").value("S"))
        .andExpect(jsonPath("$.category").value("HOME"));
  }

  @Test
  void create_sends_message() throws Exception {
    // TODO assert message is sent with testListener.blockingReceive(ofSeconds(5));
  }

  // TODO test that created product has createdDate today.
}
