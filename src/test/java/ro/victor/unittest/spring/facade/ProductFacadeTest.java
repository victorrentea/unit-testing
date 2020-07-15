package ro.victor.unittest.spring.facade;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.repo.ProductRepo;

@SpringBootTest(properties = "safety.service.url.base=http://localhost:9348")
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class ProductFacadeTest {

   @Rule
   public WireMockRule wireMockRule = new WireMockRule(9348);

   @Autowired
   private ProductFacade facade;

   @Autowired
   private ProductRepo productRepo;

   @Test(expected = IllegalStateException.class)
   public void testUnsafe() {
      Product product = new Product()
          .setExternalRef("UNSAFE");
      productRepo.save(product);

      facade.getProduct(product.getId());
   }
   @Test
   public void testSafe() {
      Product product = new Product()
          .setExternalRef("SAFE");
      productRepo.save(product);

      facade.getProduct(product.getId());
   }
}
