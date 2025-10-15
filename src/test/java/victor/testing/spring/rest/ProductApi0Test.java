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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static victor.testing.spring.entity.ProductCategory.HOME;

@Transactional
@WithMockUser(roles = "ADMIN") // grant the @Test the ROLE_ADMIN (unless later overridden)
public class ProductApi0Test extends IntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());

  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  @BeforeEach
  final void init() {
    // expected external API responses
    SafetyApiWireMock.stubResponse("barcode-safe", "SAFE");

    // reference data
    supplierRepo.save(new Supplier().setCode("S").setActive(true));
  }

  // Testing can be:
  // - Blackbox: only access API and sent/received Messages
  // - Whitebox: mock inconvenient beans
  // - Graybox: don't mock beans, but you can access DB

  @Test
  void create_select_graybox() throws Exception {
    ProductDto dto = ProductDto.builder()
        .name("Tree")
        .barcode("barcode-safe")
        .supplierCode("S")
        .category(HOME)
        .build();
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    Product product = productRepo.findByName("Tree");
    assertThat(product).isNotNull();
    // TODO 1. check product.createdDate and .barcode
  }

  @Test
//  @WithMockUser(roles = ... // Hint: downgrade credentials set at class level
  void create_failsForNonAdmin() throws Exception {
    // TODO 2. create => 403 Forbidden, and no product is saved in DB
  }

  @Test // for @Validated of @NotNull, @NotBlank, @Size...
  void create_failsValidationForMissingBarcode() throws Exception {
    // TODO 3a. create product with null barcode => 4xx Client Error containing "barcode" in body
    // TODO 3b. create product with null or empty name => 4xx

    // TODO 3x. [PRO] adjust a JSON loaded from /src/test/resource without working with a DTO instance:
    //  Canonical.load("CreateProductRequest.json").set("$.name", null).json().toString()
    //  loads src/test/resources/canonical/CreateProductRequest.json
  }

  @Test
  void create_sendMalformedJson_fails() throws Exception {
    // TODO 4. bad JSON in request payload => 500 Internal Server Error
    //  eg bad json: """ { "name: 2} """
  }

  @Test
  void get_createDateFormat() throws Exception {
    // TODO 5. prove date format is yyyy-MM-dd (eg 2025-12-25)
    //  Hint: you will need to extract that from response with a JSON Path -> ask AI
    //  This test should fail if I change the pattern in @JsonFormat(pattern = "yyyy-MM-dd") in ProductDto
  }

  @Test
  void create_get_blackbox() throws Exception {
    // TODO 6. create via API then get via API (don't access the DB)
    //  Hint: To get the created product ID, extract 'Location' response header
    //   using .andExpect(..).andReturn().getResponse().getHeader(..)
    // TODO GET /product/{id} => assert fields in response DTO
  }

  @Test
  void create_sends_message() throws Exception {
    // TODO 7. assert message is sent with testListener.blockingReceive
  }
}
