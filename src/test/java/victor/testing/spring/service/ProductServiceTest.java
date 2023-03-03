package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.TestcontainersUtils;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.*;

@SpringBootTest
@Testcontainers
//@Sql(value = "classpath:/sql/cleanup.sql",
//        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
abstract class BaseTest {
   @Container
   static public PostgreSQLContainer<?> postgres =
           new PostgreSQLContainer<>("postgres:11");
   @BeforeAll
   public static void startTestcontainer() {
      postgres.start();// TODO exercise for the reader: how to start docker ONCE for all tests!
   }

   @DynamicPropertySource
   public static void registerPgProperties(DynamicPropertyRegistry registry) {
      TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
   }
}


 class ProductService2Test extends BaseTest {
   @Test
   void explore() {
      System.out.println("Me too");
   }
}

// Nukes Spring context => +40 sec to your CI for each @Test
// don't push this unless you are testing extensions to spring.

//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)

@Transactional() // THE best cleanup for tests; if the @Transactional
// is started from @Test it behaves differently.
// It's ZOMBIE TRANSACTION from start
// When won't this work?
// - async tests @Async, CompletableFuture.supplyAsync, ThreadPoolTaskExecutor.submit()
      // !! surprise, despite using multiple threads, @Transactions work with r2dbc drivers (WebFlux),
//       as long as you don't use .subscribe()
// - @Transactional(propagation=REQUIRES_NEW) > that one will commit.
public class ProductServiceTest extends BaseTest {
   @MockBean // replaces a spring bean with a Mockito mock reset between each test and injects that here for you
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

//   @BeforeEach // not safe against multithreaded tests.
//   // the only option with non-relational stuff (Mongo, Rabbit)
//   final void before() {
//       productRepo.deleteAll();
//       supplierRepo.deleteAll(); // FK ORDER MATTERS
//   }

   @Test
   public void createThrowsForUnsafeProduct() {
      // tell .isSave() to return false when called from production code
      when(mockSafetyClient.isSafe("bar")).thenReturn(false);
      ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      // GIVEN (setup)
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN (prod call)
      productService.createProduct(dto);

      // THEN (expectations/effects)
      // Argument Captors extract the value passed from tested code to save(..)
//      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
//      verify(productRepo).save(productCaptor.capture());
//      Product product = productCaptor.getValue();


      // QUESTION TO ASK: is my database empty at start?
      // YES = piece of cake: how to do that?
      // NO = other threads are using it
      //  a) multi-threaded tests that can save you 60-70% of your CI test time (- 20m.. -6h)
      //  b) (shared test DB with all your tests of multiple developers) =2005ys

      // the strategies below only work if you find the database empty:
      // #2 find the last inserted product
      // #2 find the last product by the last PK MAX(ID)
      // #2 find the last product by creation Date
      // #3 find by supplier/name <!!> <<< fragile what if there was already a product with that DB
      // #4 findAll().get(0) <-- simplest

      // HOW Can you make sure the DB is empty?
      // if any other test COMMITS some data to DB -> you see that data
      //⭐️ but what if we never commit anything => the best (mandatory)
            // strategy to test with a relational DB. @Transactional @Test
      // if each test NEVER commits anything in the DB (docker?) and the db is empty at the start,
            // you can even run your tests in parallel


      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

   @Test
   public void createOkBis() {
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);
      productService.createProduct(dto);
      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
