package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Supplier;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractRepoTestBase {

   @Autowired
   private SupplierRepo supplierRepo;
   protected Supplier supplier;

   @BeforeEach
   public final void before() {
      supplier = supplierRepo.save(new Supplier("name"));
   }
}
