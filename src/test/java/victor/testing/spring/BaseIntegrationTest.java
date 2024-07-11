package victor.testing.spring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


// Imagine now that your Flow does not insert just a single entity
// but 25 (parent, children, audits...)
// Dream: at the end of the test anything that my test inserted/updated gets undone
@Transactional
// starts each @Test (along with all its @BeforeEach) in a transaction
// and rolls back the transaction at the end of the test
// #3 use @Transactional to foucs on the logic.
// It can miss: NOT NULL

@SpringBootTest
@ActiveProfiles("test")
// #2 for SQL-intensive projects, w/o JPA
//@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = BEFORE_TEST_METHOD)
public abstract class BaseIntegrationTest {
  public static final String SUPPLIER_CODE = "S";
  @Autowired
  protected ProductRepo productRepo;
  @Autowired
  protected SupplierRepo supplierRepo;

  @MockBean// kills performance because spring context is differently configured than
      // the one created for the other test class
      // HERE: product Mapper is a mock
      // THERE: product Mapper is a real object
  ProductMapper productMapper;// bad idea

  @BeforeEach// insert init data or using @Sql to prepopulate "reference tables"
  final void insertRefData() {
    assertThat(supplierRepo.findAll()).isEmpty();
    assertThat(productRepo.findAll()).isEmpty();
    supplierRepo.save(new Supplier().setCode(SUPPLIER_CODE)); // INSERT of data which is SELECTED by the testeed code
  }

  @AfterEach
  final void flushToCheckNotNulls() {
    System.out.println("Flushing..");
    supplierRepo.flush();
  }

//  @BeforeEach// #1 if using JPA = perfeect
//  final void insertRefData() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();// now correct
//    supplierRepo.save(new Supplier().setCode(SUPPLIER_CODE)); // INSERT of data which is SELECTED by the testeed code
//  }
//
//  @AfterEach // clean before AND after to prevent leaking data to test classes running after you
//  final void cleanup() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();// now correct
//  }
}
