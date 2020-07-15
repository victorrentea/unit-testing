package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Supplier;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"db-real", "test"})
@Transactional
public abstract class AbstractRepoTestBase {
   @Autowired
   private SupplierRepo supplierRepo;

   protected Supplier supplier = new Supplier();

   @Before
   public final void initialize() {
      System.out.println("Rulez before din super");
      supplierRepo.save(supplier);
   }
}
