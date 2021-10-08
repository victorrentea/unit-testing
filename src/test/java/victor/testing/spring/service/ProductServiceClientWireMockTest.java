package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.KIDS;

//@DataMongoTest
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@ActiveProfiles("db-mem")
public class ProductServiceClientWireMockTest {
   @Autowired
   private ProductService productService;
   @Autowired
   ProductRepo productRepo;

   private ProductDto dto = new ProductDto("::productName::",
       "::safeBarcode::", 13L, KIDS);

   @Rule
   public WireMockRule wireMock = new WireMockRule(9999);

   @Before
   public final void before() {
      productRepo.deleteAll();
   }

   @Test
   public void throwsForUnsafeProduct() {
      dto.barcode = "::unsafeBarcode::";

      IllegalStateException e = assertThrows(IllegalStateException.class,
          () -> productService.createProduct(dto));
      assertThat(e.getMessage())
          .contains("::unsafeBarcode::")
          .containsIgnoringCase("not safe");
   }
   @Test
   public void persistSafeProduct() {

      productService.createProduct(dto);

      assertThat(productRepo.count()).isEqualTo(1);
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("::productName::");
      assertThat(product.getBarcode()).isEqualTo("::safeBarcode::");
      assertThat(product.getCategory()).isEqualTo(KIDS);
   }
   @Test
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
