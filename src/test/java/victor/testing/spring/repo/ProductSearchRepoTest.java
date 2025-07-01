package victor.testing.spring.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductSearchRepoTest extends IntegrationTest {
  @Autowired
  ProductRepo repo;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  public void byName_matchExactly() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    repo.save(new Product()
        .setName("AbCd")
        .setSupplier(supplierRepo.getReferenceById(supplierId))
        .setCategory(ProductCategory.HOME));
    ProductSearchCriteria criteria = ProductSearchCriteria.empty().withName("AbCd");
    assertThat(repo.search(criteria)).hasSize(1);
  }


  @Test
  public void noCriteria() {
    // TODO
  }

  @Test
  public void byName_noMatch() {
    // TODO
  }

  @Test
  public void byName_matchLike() {
    // TODO
  }

  // TODO more @Tests?
}

