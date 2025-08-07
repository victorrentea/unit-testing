package victor.testing.spring.repo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static victor.testing.spring.entity.ProductCategory.ELECTRONICS;
import static victor.testing.spring.entity.ProductCategory.HOME;

//@Transactional
@TestInstance(PER_CLASS)// =only one class instance is used for ALL @Test [DANGER]
class ProductRepoSearchPTest extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  Product product;
  Supplier supplier;

  @BeforeAll
  final void setup() {
    supplier = supplierRepo.save(new Supplier());
    product = productRepo.save(new Product()
        .setName("NAme")
        .setSupplier(supplier)
        .setCategory(HOME));
  }
  @AfterAll
  public void cleanup() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }

  public Stream<Arguments> data() {
    return Stream.of(
        Arguments.arguments(new ProductSearchCriteria(), true),
        Arguments.arguments(new ProductSearchCriteria().setName("aM"), true),
        Arguments.arguments(new ProductSearchCriteria().setName("Ankit"), false),
        Arguments.arguments(new ProductSearchCriteria().setCategory(HOME), true),
        Arguments.arguments(new ProductSearchCriteria().setCategory(ELECTRONICS), false),
        Arguments.arguments(new ProductSearchCriteria().setSupplierId(supplier.getId()), true),
        Arguments.arguments(new ProductSearchCriteria().setSupplierId(-1L), false)

    );
  }

  @ParameterizedTest
  @MethodSource("data")
  void oneTestToRuleThemAll(ProductSearchCriteria searchCriteria, boolean matches) {
    List<ProductSearchResult> searchResults = productRepo.search(searchCriteria);

    if (matches) {
      assertThat(searchResults).hasSize(1)
          .first()
          .returns("NAme", ProductSearchResult::name)
          .returns(product.getId(), ProductSearchResult::id);
    } else {
      assertThat(searchResults).isEmpty();
    }
  }


}

