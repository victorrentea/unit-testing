//package victor.testing.spring.product.service.create;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import victor.testing.spring.product.api.dto.ProductDto;
//import victor.testing.spring.product.domain.Product;
//import victor.testing.spring.product.domain.Supplier;
//import victor.testing.spring.product.infra.SafetyClient;
//import victor.testing.spring.product.repo.ProductRepo;
//import victor.testing.spring.product.repo.SupplierRepo;
//import victor.testing.spring.product.service.ProductService;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static victor.testing.spring.product.domain.ProductCategory.HOME;
//import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;
//
//@SpringBootTest
//@ActiveProfiles({"db-mem"/*,"altu"*/})
////@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = BEFORE_TEST_METHOD) // #2
////@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // #3 niciodata n-ar trebi sa treaca de code review. -> intarzie grav Jenkins
//@Transactional // #4 auto-rollback dupa @Test daca-l pui in @Test, tx pornita pt un test este ROLLBACKED intotdeauna dupa test.
//public class CreateProduct2Test {
//  public static final String PRODUCT_NAME = "name";
//  @MockBean // inlocuieste in contextul spring pornit beanul real cu un mock de mockito
//  SafetyClient safetyClient;
//  @MockBean
//  KafkaTemplate<String, String> kafkaTemplate;
//  @Autowired
//  ProductRepo productRepo;
//  @Autowired
//  SupplierRepo supplierRepo;
//  @Autowired
//  ProductService productService;
//  //  @AfterEach // #1
////  @BeforeEach
////  public void cleanup() {
////    productRepo.deleteAll();
////    supplierRepo.deleteAll();
////  }
//
//  Long supplierId;
//  @BeforeEach
//  final void insertSupplier() {
//    supplierId = supplierRepo.save(new Supplier()).getId();
//  }
//
//  @Test
//  void createThrowsForUnsafeProduct() {
//    when(safetyClient.isSafe("unsafe")).thenReturn(false);
//    ProductDto dto = new ProductDto(PRODUCT_NAME, "unsafe", -1L, HOME);
//
//    assertThatThrownBy(() -> productService.createProduct(dto))
//        .isInstanceOf(IllegalStateException.class)
//        .hasMessage("Product is not safe: unsafe");
//  }
//  @Test
//  void createOk() {
//    // GIVEN
//    when(safetyClient.isSafe("safe")).thenReturn(true);
//    ProductDto dto = new ProductDto(PRODUCT_NAME, "safe", supplierId, HOME);
//
//    // WHEN
//    productService.createProduct(dto);
//
//    // THEN
////    Product product = productRepo.findById(productId).orElseThrow(); // ideal asa ar fi
//    Product product = productRepo.findByName(PRODUCT_NAME);
//
//    assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
//    assertThat(product.getSku()).isEqualTo("safe");
//    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
//    assertThat(product.getCategory()).isEqualTo(HOME);
//    // assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
//    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
//  }
//  @Test
//  void defaultsCategoryToUNCATEGORIZED() {
//    when(safetyClient.isSafe("safe")).thenReturn(true);
//    ProductDto dto = new ProductDto(PRODUCT_NAME, "safe", supplierId, null);
//
//    productService.createProduct(dto);
//
//    Product product = productRepo.findByName(PRODUCT_NAME);
//    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
//  }
//
//}
