package victor.testing.spring.service;

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
@TestMethodOrder(MethodName.class)
@TestInstance(PER_CLASS) // don't recreate this test class instance for each @Test
public class StatefulEpicTest {
  @Autowired
  SupplierRepo supplierRepo;

  private Long supplierId; // survives between test methods

  @BeforeAll
  public void insertInitialData() {
    supplierId = supplierRepo.save(new Supplier().setCode("S").setName("N")).getId();
  }

  @AfterAll
  public void cleanup() {
    supplierRepo.deleteById(supplierId);
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

}
