package victor.testing.spring.repo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class ProductRepoSearchTest extends IntegrationTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  static Supplier supplier;

  private record TestCase(ProductSearchCriteria criteria, boolean matches) {}

  @AfterEach
  void cleanUpDatabase() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }
//  @BeforeEach
//  static void init() {
//    supplier = supplierRepo.save(new Supplier());
//    // This method is called once before all tests, can be used for setup if needed
//  }

  @ParameterizedTest
  @MethodSource("provideTestCases")
  public void search(BiFunction<Supplier, Product, ProductSearchCriteria> func) {
    var supplier = supplierRepo.save(new Supplier());
    Product product = createValidProduct("Product1", ProductCategory.ELECTRONICS, supplier);
//    Product product2 = createValidProduct("Product2", ProductCategory.ELECTRONICS, supplier);
    Product savedProduct = productRepo.save(product);
/*    if(searchCriteria.supplierId() != null){
      searchCriteria = searchCriteria.withSupplierId(supplier.getId());
    }*/

    var results = productRepo.search(func.apply(supplier, product));

    assertThat(results).hasSize(1);
    assertThat(results.get(0).id()).isEqualTo(savedProduct.getId());
    assertThat(results.get(0).name()).isEqualTo(savedProduct.getName());
  }


  static ProductSearchCriteria createSearch(Supplier supplier, Product product) {
    return ProductSearchCriteria.builder()
        .name(product.getName())
        .category(product.getCategory())
        .supplierId(supplier.getId())
        .build();
  }


  private Product createValidProduct(String name, ProductCategory productCategory, Supplier supplier) {
    return new Product().setName(name).setSupplier(supplier).setCategory(productCategory);
  }

  private static Stream<BiFunction<Supplier, Product, ProductSearchCriteria>> provideTestCases() {
    return Stream.of(
            (Supplier supplier, Product product) -> ProductSearchCriteria.empty(),
             (Supplier supplier, Product product) -> ProductSearchCriteria.builder().name("Product1").build(),
            (Supplier supplier, Product product) -> ProductSearchCriteria.builder().category(ProductCategory.ELECTRONICS).build(),
            (Supplier supplier, Product product) -> ProductSearchCriteria.builder().supplierId(supplier.getId()).build(),
            (Supplier supplier, Product product) -> ProductSearchCriteria.builder().name("Product1").category(ProductCategory.ELECTRONICS).build(),
            (Supplier supplier, Product product) -> ProductSearchCriteria.builder().name("Product1").supplierId(supplier.getId()).build(),
            (Supplier supplier, Product product) -> ProductSearchCriteria.builder().category(ProductCategory.ELECTRONICS).supplierId(supplier.getId()).build(),
            (Supplier supplier, Product product) -> ProductSearchCriteria.builder().name("Product1").category(ProductCategory.ELECTRONICS).supplierId(supplier.getId()).build()
    );
  }


}

