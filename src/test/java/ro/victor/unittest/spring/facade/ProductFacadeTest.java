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

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

@SpringBootTest
@ActiveProfiles({"test",
    "no-who-client" // doar acest test vrea sa scoata din joc implem reala de WHO service client
}) //
@RunWith(SpringRunner.class)

// daca te gandesti sa faci:
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
// mai bine probabil cureti Springu(de ob tabelele) intr-un @Before

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
      productRepo.save(new Product("Zeama"));
      ProductSearchCriteria criteria = new ProductSearchCriteria();
      criteria.name="z";
      int n = facade.searchProduct(criteria).size();
      Assertions.assertThat(n).isEqualTo(1);
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
