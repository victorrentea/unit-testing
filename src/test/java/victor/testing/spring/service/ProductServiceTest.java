package victor.testing.spring.service;

import io.cucumber.java.Transpose;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
import victor.testing.spring.service.ProductService;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.TestcontainersUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.service.TestRepoBase.postgres;

// Motivatie: cand nu ajunge DB in mem
// 1 sa testez scripturi incrementale
// 2 am queryuri SQL native care fol featureuri spec DB mele de date


//@Testcontainers // vom porni o 🐳 Docker in care vom starta PG ca-n prod pe care voi rula toate incrementalele del a inceput
//
//@SpringBootTest//(properties = "prop.mea=alta")
////@ActiveProfiles("db-mem")
////@PeDBCurat // bun pentru baze mari
////@DirtiesContext // NU pe remote!!
//@Transactional // THE BEST pt baze relationale mici /curate <200 tabele
// fata de codu din src/main, @Transactional aici se ROLLBACK la final dupa fiecare @Test anyway
   // NU MERGE daca codul testat face @Transaction(propagation=REQUIRES_NEW/NOT_SUPPORTED)
   // NU MERGE daca async / multithreading
   //==> in aceste cazuri RENUNTA CU TOT la @Transactional din teste => recurgi la alte solutii
public class ProductServiceTest extends TestRepoBase{
//   @MockBean // in contextul pornit inlocuieste beanul real cu un mock de mockito, pe care ti-l si ijecteaza aici ca sa-l poti when/then/verify
//   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @DynamicPropertySource
   public static void registerPgProperties(DynamicPropertyRegistry registry) {
      TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
   }

//   @BeforeEach
//   final void before() {
//      // modul de lucru preferat pentru baze nerelationale nosql (eg mongo/casandra/...)
//       productRepo.deleteAll();
//       supplierRepo.deleteAll();
//   }

   // liquibase,flyway,dbmaintain...


   @Test
   public void createThrowsForUnsafeProduct() {
//      when(mockSafetyClient.isSafe("bar")).thenReturn(false);

      ProductDto dto = new ProductDto("name", "bar", -1L, ProductCategory.HOME);
      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      // GIVEN
//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      List<Product> products = productRepo.findAll();
      assertThat(products).hasSize(1);
      Product product = products.get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }
   @Test
   public void createOkBis() {
      // GIVEN
//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      Long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      List<Product> products = productRepo.findAll();
      assertThat(products).hasSize(1);
      Product product = products.get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
