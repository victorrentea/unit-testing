package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

// ROLLBACK after each @Test
@Transactional
class ProductSearch1ITest extends IntegrationTest {
  @Autowired
  ProductRepo repo;
  @Autowired
  SupplierRepo supplierRepo;

  long supplierId;

  ProductSearchCriteria criteria = new ProductSearchCriteria();

  @BeforeEach
  final void before() {
    repo.deleteAll();
    supplierRepo.deleteAll();
    supplierId = supplierRepo.save(new Supplier()).getId();
    repo.save(new Product()
            .setName("AbCd")
            .setSupplier(supplierRepo.getReferenceById(supplierId))
            .setCategory(ProductCategory.HOME)
    );
  }

  @Test
  void noCriteria() {
    assertThat(repo.search(criteria)).hasSize(1);
  }

  @Test
  void byName_noMatch() {
    criteria.setName("xyz");
    assertThat(repo.search(criteria)).isEmpty();
  }

  @Test
  void byName_matchExactly() {
    criteria.setName("AbCd");
    assertThat(repo.search(criteria)).hasSize(1);
  }

  @Test
  void byName_matchLike() {
    criteria.setName("Bc");
    assertThat(repo.search(criteria)).hasSize(1);
  }

  // covered by Parameterized and .feature files
//  @Test
//  public void bySupplier_noMatch() {
//    criteria.supplierId = -1L;
//    assertThat(repo.search(criteria)).hasSize(0);
//  }
//
//  @Test
//  public void bySupplier_match() {
//    criteria.supplierId = supplierId;
//    assertThat(repo.search(criteria)).hasSize(1);
//  }
//
//  @Test
//  public void byCategory_noMatch() {
//    criteria.category = ProductCategory.ELECTRONICS;
//    assertThat(repo.search(criteria)).hasSize(0);
//  }
//
//  @Test
//  public void byCategory_match() {
//    criteria.category = ProductCategory.HOME;
//    assertThat(repo.search(criteria)).hasSize(1);
//  }
}

