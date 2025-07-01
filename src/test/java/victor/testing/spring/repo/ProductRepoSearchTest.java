package victor.testing.spring.repo;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRepoSearchTest extends IntegrationTest {
    final static String PRODUCT_NAME = "defaultName";
    final static ProductCategory PRODUCT_CATEGORY = ProductCategory.UNCATEGORIZED;
    final static String PRODUCT_BARCODE = "defaultBarcode";
    final static String SUPPLIER_NAME = "defaultSupplier";
    @Autowired
    ProductRepo productRepo;
    @Autowired
    SupplierRepo supplierRepo;
    Supplier supplier = new Supplier().setName(SUPPLIER_NAME).setActive(true);
    Product product = new Product().setSupplier(supplier).setName(PRODUCT_NAME).setCategory(PRODUCT_CATEGORY).setBarcode(PRODUCT_BARCODE);

    Stream<TestCase> testCases() {
        return Stream.of(new TestCase(ProductSearchCriteria.empty(), true),
                         // Criteria Product Name testing
                         new TestCase(ProductSearchCriteria.builder().name(PRODUCT_NAME).build(), true),
                         new TestCase(ProductSearchCriteria.builder().name("deFaultname").build(), true),
                         new TestCase(ProductSearchCriteria.builder().name("deFaultnam").build(), true),
                         new TestCase(ProductSearchCriteria.builder().name("eFaultname").build(), true),
                         new TestCase(ProductSearchCriteria.builder().name("notExists").build(), false),
                         // Criteria Product SupplierID testing // TODO how can we test this?
                         new TestCase(ProductSearchCriteria.builder().supplierId(7L).build(), true),
                         new TestCase(ProductSearchCriteria.builder().supplierId(-1L).build(), false),
                         // Criteria Product Category testing
                         new TestCase(ProductSearchCriteria.builder().category(PRODUCT_CATEGORY).build(), true),
                         new TestCase(ProductSearchCriteria.builder().category(ProductCategory.ELECTRONICS).build(), false));
    }

    @BeforeEach
    final void beforeEach() {
        productRepo.deleteAll();
        supplierRepo.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("testCases")
        //TODO convert to @ParameterizedTest takin a TestCase param,
        //  then fully cover all branches of ProductRepoSearchImpl
        //
    void search(TestCase testCase) {
        supplierRepo.save(supplier);
        productRepo.save(product);

        List<ProductSearchResult> results = productRepo.search(testCase.criteria);

        // TODO assert matches
        assertThat(results).hasSize(testCase.matches
                                    ? 1
                                    : 0);
        assertThat(supplier.getId()).isEqualTo(testCase.criteria.supplierId());

    }

    @ParameterizedTest
    @MethodSource("testCases")
    void searchWithEmptyRepos(TestCase testCase) {
        List<ProductSearchResult> results = productRepo.search(testCase.criteria);

        assertThat(results).hasSize(0);
    }

    private record TestCase(ProductSearchCriteria criteria, boolean matches) {

    }
}

