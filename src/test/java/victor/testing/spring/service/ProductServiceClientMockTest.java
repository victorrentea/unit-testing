package victor.testing.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
@ActiveProfiles("db-mem")
public class ProductServiceClientMockTest {
   @MockBean
   public SafetyClient mockSafetyClient;

   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @Test
   public void throwsForUnsafeProduct() {
      when(mockSafetyClient.isSafe("upc")).thenReturn(false);
      Assertions.assertThrows(IllegalStateException.class, () -> {
         productService.createProduct(new ProductDto("name", "upc",-1L, ProductCategory.HOME));
      });
   }

   @Test
   public void fullOk() {
      Supplier supplier = new Supplier();
      long supplierId = supplierRepo.save(supplier).getId();
      when(mockSafetyClient.isSafe("upc")).thenReturn(true);

      long productId = productService.createProduct(new ProductDto("name", "upc", supplierId, ProductCategory.HOME));

      Product product = productRepo.findById(productId).get();

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getUpc()).isEqualTo("upc");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isNotNull();
   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
