package victor.testing.spring.migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "kafka.enabled=false"
})
class SupplierCodeUppercaseMigrationTest {

  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void supplierCodeUppercasedByMigration() {
    Supplier s = supplierRepo.findByName("Test Supplier Lower").orElseThrow();
    assertThat(s.getCode()).isEqualTo("ABC_TEST");
    supplierRepo.delete(s);
  }
}
