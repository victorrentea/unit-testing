package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;

// test slice does not start all spring but only 10% of it
//@WebFluxTest// REST endpoints reactive
//@DataMongoTest // mongo query
//@WebAuthorizationTest // Pincic specific to only test @Secured

//@Testcontainers // a java lib connecting the JVM process with the docker daemon on the OS, telling it to start images.
//@SpringBootTest // you boot up everything (the whole spring, 200 beans at least and 100+ autoconfiguration)

//@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = BEFORE_TEST_METHOD) // #2

//@Transactional //#3 SQL, when placed on a Test, @Transactional ROLLS BACK everything after each test
// starts each test in a tx already marked for rollback
// + no need to worry WHAT you inserted in RDB
// + you could run these tests in parallel, lowering the runtime of your Integration tests with up to 75%
// - the test cannot cover @Async or @Transaction(NEW)
// - you fail to see the TX commit (some DBchecks might not run in your tests)
@VictorsTest
// NUKES Spring, forcing a re-start of the Spring Boot for tests (+10..30 sec per @Test)
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // only use it for testing spring extension
// does NOT work here because the PG in a 🐳 is OUTSIDE the spring.
// cleans embeeded stuff (H2, Mongo, Kafka, Rabbit)
//@ActiveProfiles("test")
public class CreateProduct2Test extends IntegrationTest {
  public static final String PRODUCT_NAME = "name";
  @MockBean // put a mockito mock in Spring instead of the real bean implementation
  SafetyClient safetyClient;

  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;// = mock(KafkaTemplate.class);

  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  @Autowired
  ProductService productService;// = new ProductService(
  // safetyClient :
  // a) use the expections.json as you do in Component Tests
  // b) manually program a MockServer writing java code
  // c) mock the bean (Pro: you do not have to create JSON responses of external APIs)

  //  productRepo, supplierRepo, new ProductMapper(), kafkaTemplate);


//  @BeforeEach // clean#1, also works for
//    cleaning mongo doc,
//    draining rabbit queues,
//    clearing caches
//  @AfterEach
//  public void cleanup() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();
//  }

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyClient.isSafe("unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto(PRODUCT_NAME, "unsafe", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: unsafe");
  }

  @Test
  void createOk() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();// Test Data Factory (SupplierData.aSupplier())
    when(safetyClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto(PRODUCT_NAME, "safe", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
    Product product = productRepo.findByName(PRODUCT_NAME);
    assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
    assertThat(product.getSku()).isEqualTo("safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
     assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void missingCategoryDefaultsToUNCATEGORIZED() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();// Test Data Factory (SupplierData.aSupplier())
    when(safetyClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto(PRODUCT_NAME, "safe", supplierId, null);

    productService.createProduct(dto);

    Product product = productRepo.findByName(PRODUCT_NAME);
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
