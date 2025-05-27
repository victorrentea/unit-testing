package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;
import victor.testing.tools.PrettyTestNames;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static victor.testing.spring.entity.ProductCategory.HOME;

@WithMockUser(roles = "ADMIN")
@TestMethodOrder(MethodName.class) // in order step1_.., step2_.., step3_..
@TestInstance(PER_CLASS)
@PrettyTestNames
public class ProductEpicITest extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ApiTestDSL api;

  String supplierName = "S"+System.currentTimeMillis();

  ProductDto productDto = ProductDto.builder()
      .name("Tree")
      .barcode("barcode-safe")
      .supplierCode(supplierName)
      .category(HOME)
      .build();
  private Long productId;

  @BeforeAll // before the first test
  void insertInitialData() {
    supplierRepo.save(new Supplier().setCode(supplierName).setActive(true));
  }

  @BeforeEach
  final void setupWireMock() {
    SafetyApi.stubResponse("barcode-safe", "SAFE");
  }

  @Test
  void step1_create() {
    api.createProduct(productDto.withName("Tree"));
  }

  @Test
  void step2_search() {
    List<ProductSearchResult> response = api.searchProduct(ProductSearchCriteria.empty().withName("Tree"));
    assertThat(response).map(ProductSearchResult::name).containsExactly("Tree");
    productId = response.get(0).id();
  }

  @Test
  void step3_getDetails() {
    ProductDto response = api.getProduct(productId);
    assertThat(response)
        .returns(productDto.name(), ProductDto::name)
        .returns(productDto.barcode(), ProductDto::barcode)
        .returns(productDto.category(), ProductDto::category);
  }

  @Test
  void step4_update() {
    api.deleteProduct(productId);
  }

  @Test
  void step5_search() {
    List<ProductSearchResult> response = api.searchProduct(ProductSearchCriteria.empty().withName("Tree"));
    assertThat(response).isEmpty();
  }
}
