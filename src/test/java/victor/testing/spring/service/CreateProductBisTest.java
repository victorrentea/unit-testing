package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;

@SpringBootTest
@ActiveProfiles("db-mem")
@Sql(scripts = "classpath:/sql/cleanup.sql") //#2 for monster DB schemas

//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // #3 NUKE Spring before each @Test
//  avoid it by all means, it hurts the entire team. don't have on CI

//@Transactional // starts a tx for each @Test, runs all @BeforeEach in the same tx
// unlike production, after each test method, tx rollsback automatically
// + very simple
// - NOT works IF @Transactional(propagation = REQUIRES_NEW) @Async
// - MISS STUFF ('false positive test') because you never see the COMMIT:
//     - jpa flush (UQ violation)🤔. if in your @Test after the prod call you run any SELECT, this forces JPA to auto-flush oending changes
//     - @TransactionEventListener(AFTER_COMMIT)
public class CreateProductBisTest {
  @Autowired // it replaces in the spring context the real repo with a mock that you can configure
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockBean
  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductService productService;

  // #1 before/after manual cleanup - good for noSQL
//  @BeforeEach
//  @AfterEach
//  final void before() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll(); // in the correct FK direction
//    for (String cacheName : cacheManager.getCacheNames()) {
//      cacheManager.getCache(cacheName).clear();
//    }
//  }
//  @Autowired
//  private CacheManager cacheManager;

  // disable @Async multithreading for this test class
//  @TestConfiguration
//  @EnableAsync(annotation = DirtiesContext.class)
//  static class DisableAsync{}

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyClient.isSafe("upc-unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "upc-unsafe", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(safetyClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safe", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    System.out.println("After prod call");

////    productRepo.findById(createdId) // ideal
////    List<Product> list = productRepo.findAll(); // .get(0);
    Product product = productRepo.findByName("name"); // causes JPA to flush dirty changes RISK Unique?
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getUpc()).isEqualTo("safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic
    //assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");

    // @MockBean stubbing race condition
    // the same Mockito mock is ran on 3 threads, verifying would compete with others verifiy
  }

  @Test
  void defaultsToUncategorizedWhenMissingCategory() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(safetyClient.isSafe("safe")).thenReturn(true);
    // Option1: separate data sets per test. powerful in E2E
    // option2: clean the DB in between @Tests:
    ProductDto dto = new ProductDto("name", "safe", supplierId, null);

    productService.createProduct(dto);

    Product product = productRepo.findByName("name"); // RISK Unique?
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
