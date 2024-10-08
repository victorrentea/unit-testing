package victor.testing.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.tools.IntegrationTest;
import victor.testing.entity.Product;
import victor.testing.entity.ProductCategory;
import victor.testing.entity.Supplier;
import victor.testing.api.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional // ROLLBACK after each @Test
public class ProductSearch1ITest extends IntegrationTest {
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
  public void noCriteria() {
    assertThat(repo.search(criteria)).hasSize(1);
  }

  @Test
  public void byName_noMatch() {
    criteria.name = "xyz";
    assertThat(repo.search(criteria)).hasSize(0);
  }

  @Test
  public void byName_matchExactly() {
    criteria.name = "AbCd";
    assertThat(repo.search(criteria)).hasSize(1);
  }

  @Test
  public void byName_matchLike() {
    criteria.name = "Bc";
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

