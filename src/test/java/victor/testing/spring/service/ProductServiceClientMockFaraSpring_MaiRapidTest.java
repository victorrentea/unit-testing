package victor.testing.spring.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.KIDS;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceClientMockFaraSpring_MaiRapidTest {
   @Mock
   SafetyClient safetyClient;
   @Mock
   ProductRepo productRepo;
   @InjectMocks
   ProductService productService;
   @Captor
   ArgumentCaptor<Product> productCaptor;

   private ProductDto dto = new ProductDto("::productName::", "::barcode::", 13L, KIDS);

   @Before
   public final void before() {
      productService.cevaDinProps = "a";
      when(safetyClient.isSafe("::barcode::")).thenReturn(true);
   }
   @Test
   public void throwsForUnsafeProduct() {
      when(safetyClient.isSafe("::barcode::")).thenReturn(false);

      IllegalStateException e = assertThrows(IllegalStateException.class,
          () -> productService.createProduct(dto));
      assertThat(e.getMessage())
          .contains("::barcode::")
          .containsIgnoringCase("not safe");
   }
   @Test
   public void persistSafeProduct() {

      productService.createProduct(dto);

      verify(productRepo).save(productCaptor.capture());
      Product product = productCaptor.getValue();

      assertThat(product.getName()).isEqualTo("::productName::");
      assertThat(product.getBarcode()).isEqualTo("::barcode::");
      assertThat(product.getCategory()).isEqualTo(KIDS);
   }

   @Test
   public void persistsHOME_whenCategoryIsNull() {
      dto.category = null;

      productService.createProduct(dto);

      verify(productRepo).save(productCaptor.capture());
      assertThat(productCaptor.getValue().getCategory()).isEqualTo(HOME);
   }





   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
