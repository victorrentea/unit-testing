package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;

// same @SQL technique to insert static data into 'reference tables'
//@CleanupDB

// #3 nuke the entire Spring Context, force it to reboot, along with another fresh DB inside.
//   problem: + 10 - 45 seconds more / @Test -> kills the CI
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

// #4 @Transactional
// for each individual @Test and all its @BeforeEach and after the test issue a ROLLBACK
// + no need to manual cleanup
// + can run in parallel
// ⭐️THE BEST WAY TO TEST WITH A SQL DB
// limitation: when the tested prod code:
// - in prod code  @Transactional(propagation = Propagation.REQUIRES_NEW | NOT_SUPPORTED)
// - when prod code runs a different thread than the test code (eg @Async)

@Transactional // tell spring to start a separate transaction
@SpringBootTest
@ActiveProfiles("db-mem")
public class CreateProduct2Test {
  @MockBean // @Mock + @Bean = wherever SafetyClient is injected, the mock is passed in
  SafetyClient mockSafetyClient;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

  // #1 before/after cleanup - JPA only solution
  // NEVER USE THIS for SQL-> only use for NON-transactional resources
//  @BeforeEach
//  @AfterEach
//  public void method() {
//    // impossible to run these tests anymore in parallel ! -> race condition
//    productRepo.deleteAll();// in the reverse FK order
//    supplierRepo.deleteAll();
//  }

  @Test
  void createThrowsForUnsafeProduct() {
    when(mockSafetyClient.isSafe("bar")).thenReturn(false);

    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);
    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void createOk() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);

    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS)); // uses Spring Magic
  }

  @Test
  void createOkUncategorized() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, null);

    // WHEN
    productService.createProduct(dto);

    // THEN
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
