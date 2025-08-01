package victor.testing.spring.rest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.SafetyApiWireMock;
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
@TestMethodOrder(MethodName.class) // in lexicografic order: step1_.., step2_.., step3_..
@TestInstance(PER_CLASS)
public class ProductApiEpicITest extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ApiTestClient api;

  ProductDto productDto = ProductDto.builder()
      .name("Tree")
      .barcode("barcode-safe")
      .supplierCode("S")
      .category(HOME)
      .build();

  Long productId;

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
  void step10_create() {
    SafetyApiWireMock.stubResponse("barcode-safe", "SAFE");
    api.createProduct(productDto.withName("Tree"));
  }

  @Test
  void step20_search() {
    List<ProductSearchResult> response = api.searchProduct(new ProductSearchCriteria().setName("Tr"));
    assertThat(response).map(ProductSearchResult::name).containsExactly(productDto.name());
    productId = response.get(0).id();
  }

  @Test
  void step30_getDetails() {
    ProductDto response = api.getProduct(productId);
    assertThat(response)
        .returns(productDto.name(), ProductDto::name)
        .returns(productDto.barcode(), ProductDto::barcode)
        .returns(productDto.category(), ProductDto::category);
  }


  @Test
  void step40_update() {
    api.deleteProduct(productId);
  }

  //@Test
  void step45_getDetails_returnsChanges() {
    // TODO
  }

  @Test
  void step50_search_returnsChanges() {
    List<ProductSearchResult> response = api.searchProduct(new ProductSearchCriteria().setName("Tree"));
    assertThat(response).isEmpty();
  }

  //@Test
  void step60_delete() {
    // TODO
  }
  //@Test
  void step70_search_doesNotReturnIt() {
    // TODO
  }
}
