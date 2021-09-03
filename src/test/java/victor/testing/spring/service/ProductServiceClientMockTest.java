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
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("db-mem")
@SpringBootTest(properties = "safety.service.url.base=http://myMockServer:port/")
//@ExtendWith(MockitoExtension.class)
public class ProductServiceClientMockTest {
   @MockBean
   public SafetyClient mockSafetyClient/* = Mockito.mock(SafetyClient.class)*/;
//   @Mock
   @Autowired
   private ProductRepo productRepo;
//   @Mock
   @Autowired
   private SupplierRepo supplierRepo;
//   @InjectMocks
   @Autowired
   private ProductService productService;

   @Test
   public void createThrowsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         when(mockSafetyClient.isSafe("bar")).thenReturn(false);
         productService.createProduct(new ProductDto("name", "bar",-1L, ProductCategory.HOME));
      });
      // option 1 (the most robust): using that mockServer : make it return a json stored in your src/test/resources
      // option 2 (the most comfortable): replace the SafetyClient spring bean with a Mock:::
   }

   @Test
   public void createOk() {
      Long supplierId = supplierRepo.save(new Supplier()).getId();
//      when(supplierRepo.getOne(supplier.getId())).thenReturn(supplier);
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      productService.createProduct(new ProductDto("name", "safebar", supplierId, ProductCategory.HOME));

      // Yuck! - capturing an argument given by prod  to a mock
//      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
//      verify(productRepo).save(productCaptor.capture());
//      Product product = productCaptor.getValue();

      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
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
