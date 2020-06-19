package ro.victor.unittest.spring.facade;

//import org.junit.Before;
//import org.junit.Test;
//import org.junit.Test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.repo.SupplierRepo;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductFacadeTest {

   @Autowired
   private ProductFacade facade;

   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;

   @Test
   @Transactional
   public void getProduct() {
      Supplier supplier = new Supplier("emag")
          .setActive(true);
      supplierRepo.save(supplier);
      Product product = new Product("Ceapa");
      product.setSupplier(supplier);
      productRepo.save(product);
//      WhoServiceClientForTests.vaccineExists = true;

      facade.getProduct(product.getId());
   }
   @Test
   public void searchProduct() {
      facade.searchProduct(new ProductSearchCriteria());
   }
}
