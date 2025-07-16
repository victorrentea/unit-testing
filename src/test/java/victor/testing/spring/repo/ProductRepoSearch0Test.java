package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

  @BeforeEach
  @AfterEach
  void cleanup() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }
  @Test
  void search() {
    var supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product()
        .setName("Name")
        .setSupplier(supplier)
        .setCategory(ProductCategory.HOME));
    var searchCriteria = ProductSearchCriteria.empty();

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1);
  }
  @Test
  void searchNotMatchByName() {
    var supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product()
        .setName("Name")
        .setSupplier(supplier)
        .setCategory(ProductCategory.HOME));
    var searchCriteria = ProductSearchCriteria.empty()
        .withName("Other");

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).isEmpty();
  }

  // TODO 2 write a second @Test to prove search does NOT return the product in DB
  //  Example: when searching by a different name

  // TODO Write more @Test-s to fully cover ProductRepoSearchImpl (as shown by Coverage)
  //  Pro Tip: any line of code you change in tested code should fail a test


  // TODO 4 replace all tests with a single @ParameterizedTest taking a param of this type:
  record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {
  }

}

