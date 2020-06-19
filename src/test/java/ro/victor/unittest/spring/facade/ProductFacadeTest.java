package ro.victor.unittest.spring.facade;

//import org.junit.Before;
//import org.junit.Test;
//import org.junit.Test;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.repo.SupplierRepo;

import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

@SpringBootTest
@ActiveProfiles({"test",
    "no-who-client" // doar acest test vrea sa scoata din joc implem reala de WHO service client
}) //
@RunWith(SpringRunner.class)
public class ProductFacadeTest {

   @Autowired
   private ProductFacade facade;

   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;

   @Test
   @DirtiesContext(methodMode = BEFORE_METHOD)
   public void searchProduct() {
      int n = facade.searchProduct(new ProductSearchCriteria()).size();
      Assertions.assertThat(n).isEqualTo(0);
   }
   @Test
//   @Transactional
   public void getProduct() {
//      Supplier supplier = new Supplier("emag")
//          .setActive(true);
//      supplierRepo.save(supplier);
      Product product = new Product("Ceapa");
//      product.setSupplier(supplier);
      productRepo.save(product);
//      WhoServiceClientForTests.vaccineExists = true;

      facade.getProduct(product.getId());
   }
}
