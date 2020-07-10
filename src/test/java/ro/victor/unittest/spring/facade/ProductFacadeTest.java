package ro.victor.unittest.spring.facade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.infra.SafetyServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.repo.RealDBRepoTest;
import ro.victor.unittest.spring.web.ProductDto;

@RunWith(SpringRunner.class)
@RealDBRepoTest
public class ProductFacadeTest {
   @Autowired
   private ProductFacade facade;
   @Autowired
   private ProductRepo productRepo;
   @MockBean
   private SafetyServiceClient client;

   @Test(expected = IllegalStateException.class)
   public void test() {
      Product product = new Product().setExternalRef("extref");
      productRepo.save(product);
      Mockito.when(client.isSafe("extref")).thenReturn(false);
      ProductDto dto = facade.getProduct(product.getId());
   }

}
