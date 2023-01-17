package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
import victor.testing.spring.web.ProductController;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.TestcontainersUtils;
import victor.testing.tools.WireMockExtension;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.*;

@ActiveProfiles("db-migration")
@Testcontainers
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@Transactional
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // ineficient pt run time pe CI
public class ProductServiceTest {
   @Container
   static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11");

   @DynamicPropertySource
   public static void registerPgProperties(DynamicPropertyRegistry registry) {
      TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
   }
//
//   @MockBean // inlocuieste in context spring clasa reala cu un mock Mockito
//   // ->te lasa sa mockuiesti metode de pe ea
//   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;

   @Autowired
   private ProductController productService;

//   @BeforeEach
//   final void before() {
//       productRepo.deleteAll();
//      supplierRepo.deleteAll();
//   }

   @RegisterExtension // porneste un server de http pe :9999 care raspunde conform cu
           // ce gaseste in /src/test/mappings/*.json
   WireMockExtension wireMock = new WireMockExtension(9999);

//   @WithMockUser(role="ADMIN")
   @Test
   public void createThrowsForUnsafeProduct() {
      // tell .isSave() to return false when called from production code
//      when(mockSafetyClient.isSafe("bar")).thenReturn(false);
      ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

      assertThatThrownBy(() -> productService.create(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      // GIVEN (setup)
//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN (prod call)
      // ce tre sa faca codu de productie ca @Transactional di test sa NU POATA curata
      // ce a facut productia: @Async sau
      productService.create(dto);

      // THEN (expectations/effects)
      // Argument Captors extract the value passed from tested code to save(..)
      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }
   @Test
   public void createOkBis() {
      // GIVEN (setup)
//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      long supplierId = supplierRepo.save(new Supplier()).getId();
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN (prod call)
      productService.create(dto);

      // THEN (expectations/effects)
      // Argument Captors extract the value passed from tested code to save(..)
      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
