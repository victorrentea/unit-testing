package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Supplier;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"db-mem", "test"})
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // brutal, don't do it
//@Category(IntegrationTest.class)
//@ContextConfiguration(initializers = WaitForDBInitializer.class)
@Transactional
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
