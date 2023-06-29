package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.BaseDatabaseTest;
import victor.testing.spring.product.api.ProductApi;
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

//@EmbeddedMongo or @Testcontainers⭐️⭐️⭐️⭐️ ?
//@SpringBootTest

//@Transactional // when put on a @Test/test class it tells Spring to
// start a new Tx for each test, run all @BeforeEach in that Tx,
// and  rollback it after the test

// WHAT ARE THE LIMITATIONS of this technique: when does it NOT work?
// - if the DB was already dirty => the @Transactional is NOT in them habit / someone else forgot
//   -> FIX: extends BaseIntegrationTest @Transactional {
// - when is it impossible to propagate the test @Transactional to a method ?
//   a) @Transactional(propagation = Propagation.REQUIRES_NEW)
//   b) calling that method async in a different thread (@Async or CompletableFuture..)

// Benefits of @Transactional
// - auto-cleanup
// - parallel tests.

// what bugs can @Transactional slip through? (false-negatives)
// https://dev.to/henrykeys/don-t-use-transactional-in-tests-40eb
// - LAzy Loading throwin in PROD since no @Transactional in prod code, but working in @Test @Transactinal
//   REALLY ?!
// - Prod: no repo.save() nor @Transactional;
//    But in test, if you prod.call() {product=repo.find()}; repo.findById(P_ID)-> Cart.items has the product
// - Debugging a failure finds the DB empty (due to auto-rollback)
//    Fix: @Rollback(false)
// - [dark ages, old db] PRE_COMMIT trigger never run if you start the TX in test and never COMMIT


//@ActiveProfiles("db-mem")// in memory H2 db: SQL db in the JVM memory
//@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

// @Testcontainers is a bridge between JUnit and Docker
// Why is it better to execute on a real SQL sever like in prod
// - datatypes in H2 are different, when creating schema, even if you tell H2 ;MODE=Oracle
// - custom ORA types
// - liquibase scripts for ORA will not work on H2
// - usage of ORA specific SQL features (CONNECT BY) or package FUNCTIONS > native query

//all the following force spring to start a dedicated context for this test class
//@SpringBootTest(properties = "someProp=different")
//@TestPropertySource(properties = "someProp=different")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("some")
//@MockBean
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Nuke spring > NEVER REACH "main" branch
  // anti-social behavior. only for debugging
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("wiremock")
public class ProductServiceCreateTest extends BaseDatabaseTest {
//  @MockBean
//  SafetyClient safetyClient;

  // should I write most tests based on wiremock stubs, or should I @Mock[Bean] the Adapter?
  // WIREMOCK: + you get to test the adapter 100%
  // @Mock the Adapter: + you are never exposed to UGLY FOREIGN DTOs/APIs/ JWT, SSL, .... retry
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

//  @Autowired
//  private ProductApi productApi; // same spring can be reused
//  @MockBean
//  private ProductApi productApi; // this is faked IN THIS CONTEXT

  @BeforeEach
  final void doIStartOnACleanDB() {
    assertThat(productRepo.findAll()).isEmpty();
  }

  // the best cleanup for no SQL
//  @BeforeEach
//  @AfterEach
//  final void before() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();
//  }

  @Test
  void throwsForUnsafeProduct() {
//    when(safetyClient.isSafe("bar")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: bar");
  }

  @Test
  void ok() {
    // GIVEN
//    when(safetyClient.isSafe("safebar")).thenReturn(true);
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
//    Product product = productRepo.findById(id??) // #1 best
//    Product product = productRepo.findByName("name"); // #2 use anoteher finder
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);// #3 simply assuming the DB is empty initially

    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void missingCategoryDefaultsToUNCATEGORIZED() {
//    when(safetyClient.isSafe("safebar")).thenReturn(true);
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    ProductDto dto = new ProductDto("name", "safebar", supplierId, null);

    productService.createProduct(dto);

    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);// #3 simply assuming the DB is empty initially
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
