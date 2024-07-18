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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static victor.testing.spring.entity.ProductCategory.HOME;

@WithMockUser(roles = "ADMIN")
@TestMethodOrder(MethodName.class) // step1_.., step2_.., step3_..
@TestInstance(PER_CLASS)
public class ProductApiFlowITest extends IntegrationTest {
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
  private Long productId;

  @BeforeAll // before the first test
  void insertInitialData() {
    supplierRepo.save(new Supplier().setCode("S").setActive(true));
  }

  @AfterAll // after the last
  void cleanup() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }

  @Test
  void step1_create() {
    api.createProduct(productDto.setName("Tree"));
  }

  @Test
  void step2_search() {
    List<ProductSearchResult> response = api.searchProduct(new ProductSearchCriteria().setName("Tree"));
    assertThat(response).map(ProductSearchResult::getName).containsExactly("Tree");
    productId = response.get(0).getId();
  }

  @Test
  void step3_getDetails() {
    ProductDto response = api.getProduct(productId);
    assertThat(response)
        .returns(productDto.getName(), ProductDto::getName)
        .returns(productDto.getBarcode(), ProductDto::getBarcode)
        .returns(productDto.getCategory(), ProductDto::getCategory);
  }

  @Test
  void step4_update() {
    api.deleteProduct(productId);
  }

  @Test
  void step5_search() {
    List<ProductSearchResult> response = api.searchProduct(new ProductSearchCriteria().setName("Tree"));
    assertThat(response).isEmpty();
  }
}
