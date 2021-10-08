package victor.testing.spring.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.KIDS;

//@DataMongo
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("db-mem")
public class ProductServiceClientMockTest {
   @Autowired
   private ProductService productService;
   @MockBean
   private SafetyClient safetyClient;
   @Autowired
   ProductRepo productRepo;

   private ProductDto dto = new ProductDto("::productName::", "::barcode::", 13L, KIDS);

   @Before
   public final void before() {
      when(safetyClient.isSafe("::barcode::")).thenReturn(true);
      productRepo.deleteAll(); // baza ar trebui sa fie rezervata  privata doar pentru teste
      // solutia 1: o legi al docker (+ 16 GB ceri de la sefu)
      // solutia 2: pornesti un mongo embedded in test
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

      assertThat(productRepo.count()).isEqualTo(1);
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("::productName::");
      assertThat(product.getBarcode()).isEqualTo("::barcode::");
      assertThat(product.getCategory()).isEqualTo(KIDS);
   }
   @Test
//   public void givenSafeProduct_whenCategoryNull_thenPersistsHOME() {
//   public void whenCategoryNull_thenPersistsHOME() {
   public void persistsHOME_whenCategoryIsNull() {
      dto.category = null;

      productService.createProduct(dto);

      assertThat(productRepo.count()).isEqualTo(1);
      assertThat(productRepo.findAll().get(0).getCategory()).isEqualTo(HOME);
   }





   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
