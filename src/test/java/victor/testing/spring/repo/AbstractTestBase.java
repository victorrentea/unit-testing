package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Supplier;

@SpringBootTest
@ActiveProfiles("db-mem")
//@ClearAllTables
@Transactional
public class AbstractTestBase {
   protected Supplier supplier;
   @Autowired
   private SupplierRepo supplierRepo;

   @BeforeEach
   public void initialize() {
       supplier = supplierRepo.save(new Supplier("IKEA"));
   }
}
