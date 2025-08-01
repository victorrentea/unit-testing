package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRepoSearch0Test extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void search() {
    Supplier supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product()
        .setName("Name")
        .setSupplier(supplier)
        .setCategory(ProductCategory.HOME));
    var searchCriteria = new ProductSearchCriteria();

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    // TODO 1 assert the inserted product is returned
    assertThat(searchResults)/*.TODO*/;
  }

  // TODO 2 + @Test search returns product if searched by its name
  // TODO 3 + @Test search does NOT return the product if search by a DIFFERENT name

  // TODO Add more tests until ProductRepoSearchImpl gets Coverage = 100%
  //  Pro (mutation testing): any line you change in tested code should cause a test to fail



  // TODO 4 🥋 replace all tests with a single @ParameterizedTest taking a param of this type:
  record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {
  }
}

