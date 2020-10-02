package ro.victor.unittest.spring.facade;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.infra.SafetyServiceClient;
import ro.victor.unittest.spring.repo.AbstractRepoTestBase;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.ProductDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "safety.service.url.base=http://localhost:9987")
public class ProductFacadeNoMocksTest extends AbstractRepoTestBase {
   @Autowired
   private ProductFacade facade;
   @Autowired
   private ProductRepo productRepo;

   @Rule
   public WireMockRule wireMock = new WireMockRule(9987);

   @Test(expected = IllegalStateException.class)
   public void throwsForUnsafeProduct() {
      Product product = new Product()
          .setExternalRef("UNSAFE");
      productRepo.save(product);
      facade.getProduct(product.getId());
   }

   @Test
   public void throwsForInactiveSupplier() {
      Product product = new Product()
          .setExternalRef("SAFE")
          .setSupplier(new Supplier()
              .setActive(false));
//      when(safetyClient.isSafe("exref")).thenReturn(true);
      productRepo.save(product);
      IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
          facade.getProduct(product.getId()));
      assertThat(ex.getMessage()).containsIgnoringCase("supplier");
   }
   @Test
   public void ok() {
      Product product = new Product()
          .setExternalRef("SAFE")
          .setName("product-name")
          .setSupplier(new Supplier()
              .setActive(true));

//      when(safetyClient.isSafe("exref")).thenReturn(true);
      productRepo.save(product);

      ProductDto dto = facade.getProduct(product.getId());

      assertEquals("product-name", dto.productName);
      String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      assertThat(dto.sampleDate).startsWith(todayStr);
   }

}
