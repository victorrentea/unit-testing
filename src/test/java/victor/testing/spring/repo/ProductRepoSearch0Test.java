package victor.testing.spring.repo;

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

class ProductRepoSearch0Test extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void searchEmptyCriteria() {
    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    assertThat(searchResults).hasSize(1)
        .first()
        .returns("NAme", ProductSearchResult::name)
        .returns(product.getId(), ProductSearchResult::id);
  }

  // TODO 2 + @Test search returns product if searched by its name
  // TODO 3 + @Test search does NOT return the product if search by a DIFFERENT name

  // TODO 4 ++ Add more tests until ProductRepoSearchImpl gets Coverage = 100%
  // Pro (mutation testing): any line you change in tested code should cause a test to fail

  // TODO 5 🥋 duplicate this test class and replace all @Test with a single @ParameterizedTest taking this param:
  // Tip: to make your @MethodSource non-static, add on test class @TestInstance(Lifecycle.PER_CLASS)
  record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {
  }
}

