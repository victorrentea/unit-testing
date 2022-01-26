package victor.testing.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.*;


@SpringBootTest//(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles({"db-mem"})
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
   public void createThrowsForUnsafeProduct() {
      Assertions.assertThatThrownBy(() -> {
         when(mockSafetyClient.isSafe("bar")).thenReturn(false);
         productService.createProduct(new ProductDto("name", "bar", -1L, ProductCategory.HOME));
      });
   }

   @Test
   public void createOk() {
      Supplier supplier = supplierRepo.save(new Supplier());
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      productService.createProduct(new ProductDto("name", "safebar", supplier.getId(), ProductCategory.HOME));

      assertThat(productRepo.count()).isEqualTo(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isNotNull();
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
