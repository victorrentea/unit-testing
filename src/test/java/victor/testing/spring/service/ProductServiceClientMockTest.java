package victor.testing.spring.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.web.dto.ProductDto;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceClientMockTest {
   @Autowired
   private ProductService productService;

   @Test
   public void test() {
      ProductDto dto = new ProductDto("copac", "::barcode::", 13L, ProductCategory.KIDS);

      productService.createProduct(dto);
   }





   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
