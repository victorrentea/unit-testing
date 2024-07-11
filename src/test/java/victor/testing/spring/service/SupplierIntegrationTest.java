package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@SpringBootTest
@ActiveProfiles("test")
class SupplierIntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void insertingTwoSuppliersWithTheSameCodeFails() {
    supplierRepo.save(new Supplier().setCode("S1"));
    assertThatThrownBy(() -> supplierRepo.save(
        new Supplier().setCode("S1")))
        .isInstanceOf(Exception.class);
  }

}