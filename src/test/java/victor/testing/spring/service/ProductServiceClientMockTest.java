package victor.testing.spring.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceClientMockTest {
   @Autowired
   private ProductService productService;
   @MockBean
   private SafetyClient safetyClient;
   @Autowired
   ProductRepo productRepo;

   @Test
   public void throwsForUnsafeProduct() {
      ProductDto dto = new ProductDto("::productName::", "::barcode::", 13L, ProductCategory.KIDS);
      when(safetyClient.isSafe("::barcode::")).thenReturn(false);

      IllegalStateException e = assertThrows(IllegalStateException.class,
          () -> productService.createProduct(dto));
      assertThat(e.getMessage())
          .contains("::barcode::")
          .containsIgnoringCase("not safe");
   }
   @Test
   public void SAsksajkjdksajdksajdksajdksadjksadksajdksadkjdksadksadksaj() {
      ProductDto dto = new ProductDto("::productName::", "::barcode::", 13L, ProductCategory.KIDS);
      when(safetyClient.isSafe("::barcode::")).thenReturn(true);

      Long id = productService.createProduct(dto);

      Product product = productRepo.findById(id).get();
      assertThat(product.getName()).isEqualTo("::productName::");

   }





   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
