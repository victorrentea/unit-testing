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
    productRepo.save(new Product()
        .setName("Name")
        .setSupplier(new Supplier())
        .setCategory(ProductCategory.HOME));
    var searchCriteria = ProductSearchCriteria.empty();

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    // TODO 1 assert the inserted product is returned
  }

  // TODO 2 add a second @Test to prove search does NOT return the product in DB
  //  Example: when searching by a different name

  // TODO Add more @Test-s to fully cover ProductRepoSearchImpl (as shown by Coverage)
  //  Pro Tip: any line of code you change in tested code should fail a test


  // TODO 4 🥋 replace all tests with a single @ParameterizedTest taking a param of this type:
  record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {
  }

}

