package victor.testing.spring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
  public static final String SUPPLIER_CODE = "S";
  @Autowired
  protected ProductRepo productRepo;
  @Autowired
  protected SupplierRepo supplierRepo;

  @BeforeEach
  final void insertRefData() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();// now correct
    supplierRepo.save(new Supplier().setCode(SUPPLIER_CODE)); // INSERT of data which is SELECTED by the testeed code
  }

  @AfterEach // clean before AND after to prevent leaking data to test classes running after you
  final void cleanup() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();// now correct
  }
}
