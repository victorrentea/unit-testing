package ro.victor.unittest.spring.facade;

//import org.junit.Before;
//import org.junit.Test;
//import org.junit.Test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductFacadeTest {

   @Autowired
   private ProductFacade facade;

   @Test
   public void getProduct() {
//      WhoServiceClientForTests.vaccineExists = true;
      facade.getProduct(1L);
   }
   @Test
   public void searchProduct() {
      facade.searchProduct(new ProductSearchCriteria());
   }
}
