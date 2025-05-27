package victor.testing.spring.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.entity.ProductCategory.ELECTRONICS;
import static victor.testing.spring.entity.ProductCategory.HOME;

@Transactional
public class ProductSearchITest extends IntegrationTest {
  public static final String PRODUCT_NAME = "AbCd";
  @Autowired
  ProductRepo repo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ApiTestDSL api;

  long supplierId;
  Long productId;

  @BeforeEach
  final void before() {
    // Gray Box test: writes to DB
    repo.deleteAll();
    supplierId = supplierRepo.save(new Supplier()).getId();
    productId = repo.save(new Product()
        .setName(PRODUCT_NAME)
        .setSupplier(supplierRepo.getReferenceById(supplierId))
        .setCategory(HOME)
    ).getId();
  }

  static List<TestCase> testData() {
    return List.of(
        new TestCase(ProductSearchCriteria.empty(), true),
        new TestCase(ProductSearchCriteria.empty().withName(PRODUCT_NAME), true),
        new TestCase(ProductSearchCriteria.empty().withName("Bc"), true),
        new TestCase(ProductSearchCriteria.empty().withName("Xyz"), false),
        new TestCase(ProductSearchCriteria.empty().withCategory(HOME), true),
        new TestCase(ProductSearchCriteria.empty().withCategory(ELECTRONICS), false)
    );
  }

  record TestCase(ProductSearchCriteria criteria, boolean matches) {
    @SneakyThrows
    public String toString() {
      return (matches ? "found by" : "not found") + ": " + new ObjectMapper()
          .setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(criteria);
    }
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testData")
  @DisplayName("")
  void search(TestCase testCase) {
    var searchResults = api.searchProduct(testCase.criteria);
    if (testCase.matches()) {
      assertThat(searchResults)
          .containsExactly(new ProductSearchResult(productId, PRODUCT_NAME));
    } else {
      assertThat(searchResults).isEmpty();
    }
  }
}

