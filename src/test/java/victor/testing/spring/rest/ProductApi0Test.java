package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.SafetyApiWireMock;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static victor.testing.spring.entity.ProductCategory.HOME;

// grant the @Test the ROLE_ADMIN (unless later overridden)
@Transactional // pe thread
@WithMockUser(roles = "ADMIN")//pe thread
class ProductApi0Test extends IntegrationTest {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired
  MockMvc mockMvc; // emulates HTTP requests to your endpoints fara sa porneasca tomcat
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  private ProductDto dto = ProductDto.builder()
      .name("Tree")
      .barcode("barcode-safe")
      .supplierCode("S")
      .category(HOME)
      .build();

  @BeforeEach
  final void init() {
    supplierRepo.save(new Supplier().setCode("S").setActive(true)); //date de referinta "statice"
    SafetyApiWireMock.stubResponse("barcode-safe", "SAFE");
  }

  // Hint: Inspire from ApiTestClient and ProductApiEpicITest
  @Test
  void create_select_graybox() throws Exception {
    mockMvc.perform(post("/product/create")
            .content(jackson.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    Product product = productRepo.findByName("Tree");
    assertThat(product.getName()).isEqualTo("Tree");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
//    assertThat(product.getCreatedDate()).isEqualTo("2025-08-01"); maine pica
    assertThat(product.getCreatedDate()).isToday();
    //daca ar fi fost local date time
    assertThat(product.getCreatedDate()).isCloseTo(now(), byLessThan(1, SECONDS));
  }

  @Test
  void mockuriDeStatice() throws Exception {
    LocalDate craciun = LocalDate.parse("2025-12-25");

    LocalDate actual;
    try (MockedStatic<LocalDate> staticMock = Mockito.mockStatic(LocalDate.class)) {
      staticMock.when(() -> LocalDate.now()).thenReturn(craciun);
      actual = dinProd();
    }

    assertThat(actual).isEqualTo(craciun);
  }

//  @Autowired
//  Clock
  public LocalDate dinProd() {
    // daca vcerrea de viza e mai veche de 24 de ore -> dai alarma
    return LocalDate.now();
  }

  @Test
//  @WithMockUser(roles = ... // TODO downgrade credentials set at class level
  void create_failsForNonAdmin() throws Exception {
    // TODO => 403 Forbidden
  }

  @Test // for @Validated of @NotNull, @NotBlank, @Size...
  void create_failsValidationForMissingBarcode() throws Exception {
    // TODO 1 create product with null barcode => 4xx Client Error containing "barcode" in body
    // TODO 2 create product with null or empty name => 4xx Client Error
    // TODO 3 adjust a JSON loaded from /src/test/resource without working with a DTO instance:
    //  Canonical.load("CreateProductRequest").set("$.name", null).json().toString()
    //  loads src/test/resources/canonical/CreateProductRequest.json
  }

  @Test
  void create_sendBadJson_fails() throws Exception {
    // TODO bad JSON request payload  { camp": "val" }=> 500 Internal Server Error
  }

  @Test
  void create_get_blackbox() throws Exception {
    // TODO create then get the product via API (without accessing the DB)
    //  Tip: extract 'Location' using .andExpect(..).andReturn().getResponse().getHeader(..)
    // TODO GET /product/{id} => assert fields in response DTO
  }

  @Test
    // test @JsonFormat
  void get_createDateFormat() throws Exception {
    // TODO check date format is yyyy-MM-dd (eg 2025-12-25)
  }

  // TODO created product has createdDate today.

}
