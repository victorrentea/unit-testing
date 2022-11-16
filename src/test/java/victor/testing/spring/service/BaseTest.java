package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.tools.TestcontainersUtils;

@Testcontainers
@SpringBootTest
@Transactional // makes sure that each @Test + @BeforeEach/After they execute will run in a dedicated Tx, that is automatically ROLLEDBACK
@ActiveProfiles("db-migration") // enables a set of profiles for this test class
public abstract class BaseTest {

  static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
          .withReuse(true);
  @BeforeAll
  public static void startTestcontainer() {
    postgres.start();
  }

  @DynamicPropertySource
  public static void registerPgProperties(DynamicPropertyRegistry registry) {
    TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
  }

  protected Long supplierId;
  @Autowired
  protected SupplierRepo supplierRepo;


  @BeforeEach
  final void insertSupplier() {
//    supplierRepo.deleteAll();
    supplierId = supplierRepo.save(new Supplier()).getId();
  }

}
