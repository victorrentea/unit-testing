package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.entity.ProductCategory.ELECTRONICS;
import static victor.testing.spring.entity.ProductCategory.HOME;

@Transactional
public class ProductSearch2ParameterizedITest extends IntegrationTest {
  @Autowired
  ProductRepo repo;
  @Autowired
  SupplierRepo supplierRepo;

  long supplierId;

  @BeforeEach
  final void before() {
    repo.deleteAll();
    supplierRepo.deleteAll();

    supplierId = supplierRepo.save(new Supplier()).getId();
    repo.save(new Product()
            .setName("AbCd")
            .setSupplier(supplierRepo.getReferenceById(supplierId))
            .setCategory(HOME)
    );
  }

  public static List<TestCase> testData() {
    return List.of(
            new TestCase(ProductSearchCriteria.empty(), true),
//            new TestCase(criteria().setName("AbCd"), true),
//            new TestCase(criteria().setName("Bc"), true),
//            new TestCase(criteria().setName("Xyz"), false)
            new TestCase(ProductSearchCriteria.empty().withCategory(HOME), true), // covered by the .feature file
            new TestCase(ProductSearchCriteria.empty().withCategory(ELECTRONICS), false)
        // supplier covered by Feature test
    );
  }
  private record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {

  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testData")
  void search(TestCase testCase) {
    assertThat(repo.search(testCase.criteria)).hasSize(testCase.matches?1:0);
  }
}

