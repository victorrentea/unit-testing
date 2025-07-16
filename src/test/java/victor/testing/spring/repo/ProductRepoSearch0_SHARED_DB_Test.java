package victor.testing.spring.repo;

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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRepoSearch0_SHARED_DB_Test extends IntegrationTest {
  public static final long SUPPLIED_ID = 42L; //cunoscut ca e mereu acolo in DB
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  private Long productId;

  // [RemoteTestDB] daca ai un ORA MARE cu PL/SQL in el cu care vrei sa testezi
  // Risk#1: sa stergi datele
  // Risk#2: sa race cu alte teste/developeri

  @AfterEach
  void cleanup() {
    productRepo.deleteById(productId);
  }

  Supplier supplier;
  @BeforeEach
  final void insertData() {
    supplier = supplierRepo.findById(SUPPLIED_ID).orElseThrow();
    productId = productRepo.save(new Product()
        .setName("Name" + UUID.randomUUID())
        .setSupplier(supplier)
        .setCategory(ProductCategory.HOME)).getId();

  }
  @Test
  void searchWithNoCriteria() {
    var searchCriteria = ProductSearchCriteria.empty();

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults)
        .extracting(ProductSearchResult::id)
        .contains(productId);
  }
  @Test
  void searchNotMatchByName() {
    var searchCriteria = ProductSearchCriteria.empty()
        .withName("Other");

    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults)
        .extracting(ProductSearchResult::id)
        .doesNotContain(productId);
  }

}

