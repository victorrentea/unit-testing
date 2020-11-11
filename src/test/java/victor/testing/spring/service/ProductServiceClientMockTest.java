package victor.testing.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
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

@Component
class TestDataInserter {

}

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
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
      Assertions.assertThrows(IllegalStateException.class, () -> {
         when(mockSafetyClient.isSafe("upc")).thenReturn(false);
         productService.createProduct(new ProductDto("name", "upc",-1L, ProductCategory.HOME));
      });
   }

   @Test
   @WithStaticData
   public void fullOk() {
      long supplierId = 1L;//supplierRepo.save(supplier).getId();
      when(mockSafetyClient.isSafe("upc")).thenReturn(true);

      ProductDto dto = new ProductDto("name", "upc", supplierId, ProductCategory.HOME);
      long productId = productService.createProduct(dto); // prod call

      Product product = productRepo.findById(productId).get();

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getUpc()).isEqualTo("upc");
      assertThat(product.getSupplier().getId()).isEqualTo(1L);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);

      assertThat(product.getCreateDate()).isNotNull();
   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
