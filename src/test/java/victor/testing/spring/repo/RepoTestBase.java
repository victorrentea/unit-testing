package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Supplier;


@SpringBootTest
@ActiveProfiles("db-real")
@Transactional
//@Sql(scripts = "insert-ref-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class RepoTestBase {
   @Autowired
   private SupplierRepo supplierRepo;

   protected Supplier supplier;
   @BeforeEach
   final void before() {
      supplier = supplierRepo.save(new Supplier());
   }
}
