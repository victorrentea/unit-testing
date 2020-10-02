package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import ro.victor.unittest.spring.domain.Supplier;

public class AbstractRepoTestBase {
   @Autowired
   private SupplierRepo supplierRepo;
   protected Supplier supplier;

   @Before
   public void insertSupplier() {

      supplier = supplierRepo.save(new Supplier().setName("IKEA"));
      // dupa, the supplier entity was assigned an ID from the DB sequence
   }
}
