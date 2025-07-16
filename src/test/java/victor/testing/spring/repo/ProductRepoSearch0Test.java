package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
// cleanup #2 cand nu ai JPA
//@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

@Transactional
// 💖 Cleanup #3 in teste are intelesul: "ruleaza fiecare @Test in tx noua, pe care dupa test ROLLbACK

// # Fix#4 NU FOLOSI ☢️: H2 reporneste :
//@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD) // crima de perf pt teste de integrare
// reporneste springu pt fiecare metoda de test => +20..30s/@Test
// INTERZIS PE GIT mai mult de 2-3 (fiecare judecate cu batranu')
public class ProductRepoSearch0Test extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

//  // Cleanup #1 - general purpose pt orice res externa :Mongo, ActiveMQ, SQL, Redis..
//  @BeforeEach
//  @AfterEach
//  void cleanup() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();
//  }

  Supplier supplier;
  @BeforeEach
  final void insertData() {
    supplier = supplierRepo.save(new Supplier());
    productRepo.save(new Product()
        .setName("Name")
        .setSupplier(supplier)
        .setCategory(ProductCategory.HOME));

  }
  @Test
  void searchWithNoCriteria() {
    var searchCriteria = ProductSearchCriteria.empty();

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1);
  }
  @Test
  void searchMatchesName() {
    var searchCriteria = ProductSearchCriteria.empty()
        .withName("am");

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1);
  }
  @Test
  void searchNotMatchByName() {
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

