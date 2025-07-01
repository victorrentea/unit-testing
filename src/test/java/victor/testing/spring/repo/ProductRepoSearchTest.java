package victor.testing.spring.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRepoSearchTest extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  private record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {
  }

  @Test
  //TODO convert to @ParameterizedTest takin a TestCase param,
  //  then fully cover all branches of ProductRepoSearchImpl
  //
  public void search() {
    var supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product().setSupplier(supplier));
    var searchCriteria = ProductSearchCriteria.empty();

    var results = productRepo.search(searchCriteria);

    // TODO assert matches
  }
}

