package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;

//@Sql("classpath:/sql/cleanup.sql")
// for terrible Oracles 500 tables of love
@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional // when placed on a test class has a differnt semantics:
// the transactions started from tests are neve commited (have rollback-only flag =true)
// SURPRISE: you can actually run such @Transactional tests in paralllel (multi-threaded spring bo0t tests)
// => -70% of your CI test time.
  // + the joy: race bugs, deadlocks, no statics,
// multiple spring boot contexts existing in parallel at the same time !
// https://www.baeldung.com/junit-5-parallel-tests
public abstract class BaseDBTest {
  protected Long supplierId;
  @Autowired
  private SupplierRepo supplierRepo;

  @BeforeEach
  final void insertStaticData() {
    supplierId = supplierRepo.save(new Supplier()).getId();

  }
}
