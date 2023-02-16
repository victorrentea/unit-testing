package victor.testing.spring.service;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import javax.print.attribute.standard.RequestingUserName;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.*;

@Sql(value = "classpath:/sql/cleanup.sql",
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface WipeDB {}

 // AVOID this in normal applications: only use when you write code
// that CHANGES the spring context (eg defines new beans, changes environme)
// @DirtiesContext(classMode =  ClassMode.BEFORE_EACH_TEST_METHOD)
// tempting to use this to blow up your H2 in memory db.

@WipeDB // NOT PARALLELIZABLE
//@Transactional // parallelization-friendly

// assume we can't do this: @Async , REQUIRES_NEW, no-sql, MQ, files on disk
public class ProductServiceTest extends DBTest {
   @MockBean
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

//   @BeforeEach
//   final void before() {
//       productRepo.deleteAll(); // FK violation
//      supplierRepo.deleteAll();
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
      // GIVEN (setup)
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN (prod call)
      productService.createProduct(dto);

      // THEN (expectations/effects)
      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
