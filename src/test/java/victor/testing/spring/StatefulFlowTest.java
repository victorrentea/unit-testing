package victor.testing.spring;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodName.class) // step1_.., step2_.., step3_..
@TestInstance(PER_CLASS) // have 1 instance of this class for all tests
public class StatefulFlowTest {
  @Autowired
  SupplierRepo supplierRepo;

  Long supplierId; // survives between @Tests thanks to @TestInstance(PER_CLASS)

  @BeforeAll // before the first
  void insertInitialData() {
    Supplier s = new Supplier().setCode("S").setName("N");
    supplierId = supplierRepo.save(s).getId();
  }

  @Test
  void step1_findSupplierByName() {
    Supplier s = supplierRepo.findByName("N").orElseThrow();
    assertThat(s.getId()).isEqualTo(supplierId);
  }

  @Test
  void step2_updateSupplier() { // checks it does not throw ex
    Supplier s = supplierRepo.findById(supplierId).orElseThrow();
    s.setCode("S2");
    supplierRepo.save(s);
  }

  @Test
  void step3_findSupplierByCode() {
    Supplier s = supplierRepo.findByCode("S2").orElseThrow();
    assertThat(s.getId()).isEqualTo(supplierId);
  }

  @AfterAll // after the last
  void cleanup() {
    supplierRepo.deleteById(supplierId);
  }

}
