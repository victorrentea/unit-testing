package victor.testing.spring.product.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.ProductCategory;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.api.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
public class ProductRepoH2Test {
  @Autowired
  ProductRepo repo;
  @Autowired
  SupplierRepo supplierRepo;

  long supplierId;

  ProductSearchCriteria criteria = new ProductSearchCriteria();

  @BeforeEach
  final void before() {
    supplierId = supplierRepo.save(new Supplier()).getId();
    repo.save(new Product()
            .setName("AbCd")
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

  @Test
  public void bySupplier_noMatch() {
    criteria.supplierId = -1L;
    assertThat(repo.search(criteria)).hasSize(0);
  }

  @Test
  public void bySupplier_match() {
    criteria.supplierId = supplierId;
    assertThat(repo.search(criteria)).hasSize(1);
  }

  @Test
  public void byCategory_noMatch() {
    criteria.category = ProductCategory.ELECTRONICS;
    assertThat(repo.search(criteria)).hasSize(0);
  }

  @Test
  public void byCategory_match() {
    criteria.category = ProductCategory.HOME;
    assertThat(repo.search(criteria)).hasSize(1);
  }
  // ... so one: min 2 cases / search criteria
}

