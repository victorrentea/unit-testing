package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

//@Sql(scripts = "classpath:/sql/cleanup.sql") // cleanup #3 pt DB masive

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // Fix#4 recreaza spring context = perf hit!

//@Transactional // cleanup #2 in src/test face pe Spring sa dea ROLLBACK la finalul @Test automat
public class ProductRepoSearch0Test extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

//  @BeforeEach // #1 cleanup: nosql/orice nu merge #2/#3
//  final void before() {
//      productRepo.deleteAll(); // in ordinea FK
//      supplierRepo.deleteAll();
//  }

  @Test
  void search() {
    Supplier supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product()
        .setName("Name")
        .setSupplier(supplier)
        .setCategory(ProductCategory.HOME));
    var searchCriteria = ProductSearchCriteria.empty();

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1);
    // TODO 1 assert the inserted product is returned
  }

  @Test
  void search2() {
    Supplier supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product()
        .setName("Name")
        .setSupplier(supplier)
        .setCategory(ProductCategory.HOME));
    var searchCriteria = ProductSearchCriteria.empty();

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1);
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

