package victor.testing.spring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.tools.TestcontainersUtils;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTest {
  @Autowired
  protected SupplierRepo supplierRepo;
  @Autowired
  protected ProductRepo productRepo;

  @AfterEach
  @BeforeEach
  public void tearDown() {
    // in loc de asta, poti intr-un script
// + 10 altele

    productRepo.deleteAll();
    supplierRepo.deleteAll();
        supplierRepo.save(new Supplier().setCode("S"));
//    userRepo.deleteAll; // #sieu x 20
  }
}
