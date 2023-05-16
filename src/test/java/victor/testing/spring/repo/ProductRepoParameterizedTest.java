//package victor.testing.spring.repo;
//
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import victor.testing.spring.domain.Product;
//import victor.testing.spring.domain.ProductCategory;
//import victor.testing.spring.domain.Supplier;
//import victor.testing.spring.web.dto.ProductSearchCriteria;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static victor.testing.spring.domain.ProductCategory.ELECTRONICS;
//import static victor.testing.spring.domain.ProductCategory.HOME;
//
//@SpringBootTest
//@ActiveProfiles("db-mem")
//@Transactional
//public class ProductRepoParameterizedTest {
//  @Autowired
//  ProductRepo repo;
//  @Autowired
//  SupplierRepo supplierRepo;
//
//  long supplierId;
//
//  @BeforeEach
//  final void before() {
//    supplierId = supplierRepo.save(new Supplier()).getId();
//    repo.save(new Product()
//            .setName("AbCd")
//            .setSupplier(supplierRepo.getReferenceById(supplierId))
//            .setCategory(HOME)
//    );
//  }
//
//  public static List<TestCase> testData() {
//    return List.of(
//            new TestCase(criteria(), true),
//            new TestCase(criteria().setName("AbCd"), true),
//            new TestCase(criteria().setName("Bc"), true),
//            new TestCase(criteria().setName("Xyz"), false),
//            new TestCase(criteria().setCategory(HOME), true),
//            new TestCase(criteria().setCategory(ELECTRONICS), false),
//            new TestCase(criteria().setSupplierId(-1L), false)
////            new TestCase(criteria().setSupplierId(supplierId), true) // oups: limitation
//    );
//  }
//  private record TestCase(ProductSearchCriteria criteria, boolean matches) {}
//
//  private static ProductSearchCriteria criteria() {
//    return new ProductSearchCriteria();
//  }
//
//  @ParameterizedTest(name = "{0}")
//  @MethodSource("testData")
//  public void search(TestCase testCase) {
//    assertThat(repo.search(testCase.criteria)).hasSize(testCase.matches?1:0);
//  }
//}
//
