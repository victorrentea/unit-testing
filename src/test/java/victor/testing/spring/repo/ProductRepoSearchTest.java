package victor.testing.spring.repo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // keeps the same instance for all @Test
public class ProductRepoSearchTest extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  Long productId;
  Supplier supplier;

  record TestCase(
      ProductSearchCriteria criteria,
      boolean matches) {
  }


  @BeforeAll
  final void before() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();

    supplier = supplierRepo.save(new Supplier());
    productId = productRepo.save(new Product()
            .setName("JustforTestingname")
            .setCategory(ProductCategory.HOME)
            .setSupplier(supplier))
        .getId();
  }

  Stream<TestCase> testCase() {
    return Stream.of(
        new TestCase(ProductSearchCriteria.empty(), true),
        new TestCase(ProductSearchCriteria.builder().name("name").build(), true),
        new TestCase(ProductSearchCriteria.builder().name("JustforTestingname").build(), true),
        new TestCase(ProductSearchCriteria.builder().name("NaMe").build(), true),
        new TestCase(ProductSearchCriteria.builder().name("Just%Testing").build(), true),
        new TestCase(ProductSearchCriteria.builder().name("%Just").build(), true),
        new TestCase(ProductSearchCriteria.builder().name("*").build(), false),
        new TestCase(ProductSearchCriteria.builder().name("Not_Exist").build(), false),
        new TestCase(ProductSearchCriteria.builder().supplierId(supplier.getId()).build(), true),
        new TestCase(ProductSearchCriteria.builder().supplierId(-1L).build(), false),
        new TestCase(ProductSearchCriteria.builder().supplierId(null).build(), true),
        new TestCase(ProductSearchCriteria.builder().category(ProductCategory.HOME).build(), true),
        new TestCase(ProductSearchCriteria.builder().category(null).build(), true)
    );
  }


  //TODO convert to @ParameterizedTest takin a TestCase param,
  //  then fully cover all branches of ProductRepoSearchImpl
  //
  //parametr
  @MethodSource("testCase")
  @ParameterizedTest
  public void search(TestCase testCase) {
    var results = productRepo.search(testCase.criteria);

    if (testCase.matches) {
      assertThat(results).isNotEmpty().first().returns(productId, ProductSearchResult::id);
    } else {
      assertThat(results).isEmpty();
    }
  }

  @AfterAll
  final void after() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }
}

