package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.web.dto.ProductDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@ActiveProfiles("db-mem")
@Transactional

public class ProductServiceTest {
   @Autowired
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @RegisterExtension
   public WireMockExtension wireMock = new WireMockExtension(9999);
   @Test
   public void createThrowsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
//         when(mockSafetyClient.isSafe("bar")).thenReturn(false);
         productService.createProduct(new ProductDto("name", "bar",-1L, ProductCategory.HOME));
      });
   }

   @Test
   public void createOk() {
      Supplier supplier = new Supplier();
      supplierRepo.save(supplier);
//      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      productService.createProduct(new ProductDto("name", "safebar", supplier.getId(), ProductCategory.HOME));

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
