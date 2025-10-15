package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.After;
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
  // sau cu @Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  final void initData() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
    Supplier supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product()
        .setName("Name")
        .setSupplier(supplier)
        .setCategory(ProductCategory.HOME));
  }
  @AfterEach
  final void cleanup() { // sa nu ramana [de] la urmatorul
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }
  ProductSearchCriteria searchCriteria = new ProductSearchCriteria();
  public ProductRepoSearch0Test() {
    System.out.println("Pentru fiecare @Test, JUnit da new clasei de test! (SOC) nimeni nu se aspteapta");
  }
  @Test
  void noCriteria() {
    System.out.println("Ce criterii am " + searchCriteria);
    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1);
  }
  @Test
  void searchByName_matching() {
    searchCriteria.setName("aM");

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1);
  }
  @Test
  void searchByName_not_matching() {
    searchCriteria.setName("Costel");

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).isEmpty();
  }

  @Test
  void searchByCategory_match() {
    searchCriteria.setCategory(ProductCategory.HOME);

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1);
  }

  @Test
  void searchByCategory_noMatch() {
    searchCriteria.setCategory(ProductCategory.ELECTRONICS);

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).isEmpty();
  }

  @Test
  void searchBySupplier_noMatch() {
    searchCriteria.setSupplierId(999L);

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).isEmpty();
  }

  @Test
  void searchBySupplier_match() {
    Long existingSupplierId = supplierRepo.findAll().get(0).getId();
    searchCriteria.setSupplierId(existingSupplierId);

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1);
  }



  // TODO 4 ++ Add more tests until ProductRepoSearchImpl gets Coverage = 100%
  // Pro (mutation testing): any line you change in tested code should cause a test to fail

  // TODO 5 🥋 duplicate this test class and replace all @Test with a single @ParameterizedTest taking this param:
  // Tip: to make your @MethodSource non-static, add on test class @TestInstance(Lifecycle.PER_CLASS)
  record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {
  }
}

