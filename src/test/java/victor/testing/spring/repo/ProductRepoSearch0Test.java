package victor.testing.spring.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;

public class ProductRepoSearch0Test extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  // TODO fully cover all branches of ProductRepoSearchImpl
  void search() {
    var supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product().setSupplier(supplier));
    var searchCriteria = ProductSearchCriteria.empty();

    var results = productRepo.search(searchCriteria);

    // TODO 1 assert the list contains exactly one result with the productId
  }

  // TODO 2 write a second test proving search does NOT return the product in DB

  // TODO 3 write a third test proving search DOES return the product in DB (eg by supplierId)

  // TODO 4 replace all tests with a single @ParameterizedTest taking a param of this type:
  record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {
  }

}

