package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
//@CleanupDB
@ActiveProfiles("db-mem")
@Transactional // TOT TIMPUL testele care intearct cu o DB relationala trebuie sa aiba @Transactional ca spring dupa fiecare test sa faca ROLLBACK
public class MockitoTest {
   @MockBean // asta ii zice lui spring sa INLOCUIASCA in contextul lui beanul real SafetyClient
   // cu un mock produs de mockito, si acel mock sa-l puna si pe campul asta,m ca sa pot sa-l programez.
   public SafetyClient mockSafetyClient;// = new SafetyClient(new RestTemplate());
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;

   @Autowired
   private ProductService productService;

   @Test
   public void createThrowsForUnsafeProduct() {
      when(mockSafetyClient.isSafe("bar")).thenReturn(false);

      Assertions.assertThrows(IllegalStateException.class, () ->
          productService.createProduct(new ProductDto("name", "bar", -1L, ProductCategory.HOME)));
   }
   @Test
   public void createOk() {
      // GIVEN
      Long supplierId =supplierRepo.save(new Supplier()).getId();
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      // WHEN
      productService.createProduct(new ProductDto("name", "safebar", supplierId, ProductCategory.HOME));

      // THEN
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

   @Test
   public void createOk2() {
      // GIVEN
      Long supplierId =supplierRepo.save(new Supplier()).getId();
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      // WHEN
      productService.createProduct(new ProductDto("name", "safebar", supplierId, ProductCategory.HOME));

      // THEN
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

//   @BeforeEach
//   public void curataDupaMine() {
//      productRepo.deleteAll();
//      supplierRepo.deleteAll();
//   }

}
