package victor.testing.spring.repo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Supplier;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
public abstract class RepoTestBase {
   @Autowired
   private SupplierRepo supplierRepo;
   protected Supplier supplier;

   @Before
   public final void initDataInDB() {
      supplier = supplierRepo.save(new Supplier());
   }
}
