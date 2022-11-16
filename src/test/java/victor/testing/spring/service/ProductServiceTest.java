package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
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
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("db-migration") // enables a set of profiles for this test class
public class ProductServiceTest {
   @MockBean // this tells Spring to REPLACE in its context the real SafetyClient bean with a Mockit mock!
   // and injhect that mock in this field to allow you teach its methods what to return
   public SafetyClient mockSafetyClient;
   @MockBean
   private ProductRepo productRepo;
   @MockBean
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;


    static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
            .withReuse(true);
    @BeforeAll
    public static void startTestcontainer() {
        postgres.start();
    }

   @DynamicPropertySource
   public static void registerPgProperties(DynamicPropertyRegistry registry) {
      TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
   }

   @Test
   public void createThrowsForUnsafeProduct() {
      when(mockSafetyClient.isSafe("bar")).thenReturn(false);
      ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      // GIVEN
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      Supplier supplier = new Supplier().setId(13L);
      when(supplierRepo.findById(supplier.getId())).thenReturn(of(supplier));
      ProductDto dto = new ProductDto("name", "safebar", supplier.getId(), HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
      verify(productRepo).save(productCaptor.capture()); // extract the value passed from tested code to save(..)
      Product product = productCaptor.getValue();
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
