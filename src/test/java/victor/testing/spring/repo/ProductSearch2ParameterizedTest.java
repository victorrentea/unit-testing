package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.entity.ProductCategory.ELECTRONICS;
import static victor.testing.spring.entity.ProductCategory.HOME;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductSearch2ParameterizedTest {
  @Autowired
  ProductRepo repo;
  @Autowired
  SupplierRepo supplierRepo;

  long supplierId;

  @BeforeEach
  final void before() {
    supplierId = supplierRepo.save(new Supplier()).getId();
    repo.save(new Product()
            .setName("AbCd")
            .setSupplier(supplierRepo.getReferenceById(supplierId))
            .setCategory(HOME)
    );
  }

  private static ProductSearchCriteria criteria() {
    return new ProductSearchCriteria();
  }

  public static List<TestCase> testData() {
    return List.of(
            new TestCase(criteria(), true),
//            new TestCase(criteria().setName("AbCd"), true),
//            new TestCase(criteria().setName("Bc"), true),
//            new TestCase(criteria().setName("Xyz"), false)
            new TestCase(criteria().setCategory(HOME), true), // covered by the .feature file
            new TestCase(criteria().setCategory(ELECTRONICS), false)
        // supplier covered by Feature test
    );
  }
  private record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {

  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testData")
  public void search(TestCase testCase) {
    assertThat(repo.search(testCase.criteria)).hasSize(testCase.matches?1:0);
  }
}

