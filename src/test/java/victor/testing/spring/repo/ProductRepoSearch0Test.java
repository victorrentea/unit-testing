package victor.testing.spring.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.util.List;

public class ProductRepoSearch0Test extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void search() {
    var supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product()
        .setName("Name")
        .setSupplier(supplier)
        .setCategory(ProductCategory.HOME));
    var searchCriteria = ProductSearchCriteria.empty();

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    // TODO 1 assert the inserted product is returned
  }

  // TODO 2 write a second test to prove search does NOT return the product in DB
  //  when searching by criteria not matching the product

  // TODO Write more tests to fully cover ProductRepoSearchImpl (as shown by Coverage)
  // Pro Tip: any line of code you change in tested code should fail a test


  // TODO 4 replace all tests with a single @ParameterizedTest taking a param of this type:
  record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {
  }

}

