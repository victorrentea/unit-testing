package ro.victor.unittest.spring.facade;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.infra.SafetyServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.repo.RealDBRepoTest;
import ro.victor.unittest.spring.web.ProductDto;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest (properties = "safety.service.url.base=http://localhost:9876")
@ActiveProfiles({"db-real","dummyFileRepo"})
@Transactional
public class ProductFacadeTest {
   @Autowired
   private ProductFacade facade;
   @Autowired
   private ProductRepo productRepo;

   @Rule
   public WireMockRule wireMockRule = new WireMockRule(9876);

   @Test(expected = IllegalStateException.class)
   public void test() {
      Product product = new Product().setExternalRef("UNSAFE");
      productRepo.save(product);
      ProductDto dto = facade.getProduct(product.getId());
   }

   @Test(expected = IllegalStateException.class)
   public void tes2() {
      Product product = new Product().setExternalRef("EXTREF");
      productRepo.save(product);

      boolean b = false;
      WireMock.stubFor(get(urlEqualTo("/product/EXTREF/safety"))
          .willReturn(aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/json")
              .withBody("[{\"category\":\"DETERMINED\", \"safeToSell\":"+ b+ "}]")));

      facade.getProduct(product.getId());
   }
   @Test
   public void safe() {
      Product product = new Product()
          .setExternalRef("SAFE")
          .setSupplier(
               new Supplier().setActive(true));
      productRepo.save(product);
      ProductDto dto = facade.getProduct(product.getId());
   }

}
