package victor.testing.spring.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
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

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


// Vlad: dupa fiecare @Test sa fac ROLLBACK la tranzactia pe care am jucat @Testul meu
// (in care am inserat date in DB, selectat, testat , etc)

@Slf4j
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999") // unde asculta WireMock
@ActiveProfiles("db-migration")
@Transactional
@Testcontainers

// daca ai carca de SQL, PL/SQL, native multe si vrei sa stergi. Sau daca ai COMMITuri intermediare.
//@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

// rau, mananca timp
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // = Nukes Spring. Killareste contextul si-l forteaza sa se REPORNEASCA (banner)

//@Execution(ExecutionMode.SAME_THREAD) // !!! Atentie

@AutoConfigureWireMock(port = 9999)// ridica un server de HTTP care raspunde cu fisiere din src/test/mappings/*.json
public class ProductServiceTest {


   @Container
   static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11");

   @DynamicPropertySource
   public static void registerPgProperties(DynamicPropertyRegistry registry) {
      TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
   }


   // inlocuieste beanul real SafetyClient din Spring context
   // cu un mock Mockito, pe care ti-l si injecteaza in campul asta,
   // ca sa-i poti face ce-i faci de ob unui mock. ! intre @Test, behaviorul
   // acestui mock se reseteaza automat

//   @MockBean // no need to mock the adapter anymore : let the tested code hit WireMock instead serving baked JSON files
   // from my git.
//   public SafetyClient safetyClientMock;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   // daca tre sa cureti ceva "sa pleci de pe curat" <<< asta!
      // NU pentru Relational DBs. Asta e buna pentru orice altceva:
   // de golit cozi, cacheuri, mongo, cassandra, unic in Spring. fisiere.
   // (daca joci multithreading pe astea --- esti mort)
//   @BeforeEach
//   @AfterEach// paranoia?
//   public void curataDupaMine() {
//      productRepo.deleteAll();
//      supplierRepo.deleteAll();
//   }
   @Test
   public void createThrowsForUnsafeProduct() {
//      when(safetyClientMock.isSafe("bar")).thenReturn(false);
      productService.horror();
      ProductDto dto = new ProductDto("name", "bar", -1L, ProductCategory.HOME);
      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @SneakyThrows
   public static void staiUnPic() {
      log.info("Start");
      Thread.sleep(2000);
      log.info("End");
   }

   @Test
   public void vic_createOk() {
      // GIVEN
      // in loc de a "stabui" apelul de repo, fac un "save" inainte de a chema codul testat
      Long supplierId = supplierRepo.save(new Supplier("de ce Doamne?")).getId();
//      when(safetyClientMock.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

      staiUnPic();
      // WHEN
      productService.createProduct(dto);

      // THEN
      assertThat(productRepo.findAll()).hasSize(1); // ca tabelele sunt goale la inceput ca e DB in memorie
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

   @Test
   public void vic_createOkBis() {
      // GIVEN
      // in loc de a "stabui" apelul de repo, fac un "save" inainte de a chema codul testat
      Long supplierId = supplierRepo.save(new Supplier("de ce Doamne?")).getId();
//      when(safetyClientMock.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);


      staiUnPic();
      // WHEN
      productService.createProduct(dto);

      // THEN
      assertThat(productRepo.findAll()).hasSize(1); // ca tabelele sunt goale la inceput ca e DB in memorie
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
