package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductDto;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

public class ProductService2Test extends BaseDBTest{
   @MockBean // replaces in the springcontext the bean with a MOckito mock that you can program time+=14.5 seconds :)
   public SafetyClient mockSafetyClient;
   // what if we let the mock safety client work for real (no mocks) and we would mock
   // spring frameworks' RestTemplate ( a library class )
   // => NEVER as it will force me to mock a library class that I don;t own
   // instead I will use WireMock to start a http server answering with the JSONs I want
   @Autowired
   private ProductRepo productRepo;

   @Autowired
   private ProductService productService;

   //   @BeforeEach
//   final void before() {
//       productRepo.deleteAll();
//      supplierRepo.deleteAll(); // order matters. Listen to the FKs, Luke!
//   }
   @Test
   public void createThrowsForUnsafeProduct() {
      // tell .isSave() to return false when called from production code
      when(mockSafetyClient.isSafe("bar")).thenReturn(false);
      ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      // GIVEN (setup)
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN (prod call)
      productService.createProduct(dto);

      // THEN (expectations/effects)
      Product product = productRepo.findAll().get(0);// <=> iff the PRODUCT table was empty
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

   @Test
   public void createOkBis() {
      // GIVEN (setup)
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN (prod call)
      productService.createProduct(dto);

      // THEN (expectations/effects)
      Product product = productRepo.findAll().get(0);// <=> iff the PRODUCT table was empty
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
