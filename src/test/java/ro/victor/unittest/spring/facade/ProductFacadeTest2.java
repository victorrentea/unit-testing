package ro.victor.unittest.spring.facade;

//import org.junit.Before;
//import org.junit.Test;
//import org.junit.Test;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.infra.IWhoServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.repo.SupplierRepo;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ProductFacadeTest2 {
   @Autowired
   private ProductFacade facade;

   @Autowired
   private IWhoServiceClient client;
   @Test
   public void searchProduct() {
      System.out.println("Am fost injectat cu " + client.getClass());
      int n = facade.searchProduct(new ProductSearchCriteria()).size();
      Assertions.assertThat(n).isEqualTo(0);
   }
}
