package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;

//@EmbeddedMongo or @Testcontainers⭐️⭐️⭐️⭐️ ?
@SpringBootTest
@ActiveProfiles("db-mem")// in memory H2 db: SQL db in the JVM memory
public class ProductServiceCreate2Test {
  @MockBean // replace the spring bean with a mockito mock placing it here for programming, auto-reset between tests
  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

  // the best way to clean a no-sql db eg mongo
  @BeforeEach
  @AfterEach // not enough to protect from other Test class that forget to do it.
  public void cleanEverything() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }

  @Test
  void aathrowsForUnsafeProduct() {
    when(safetyClient.isSafe("bar")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: bar");
  }

  @Test
  void ok() {
    // GIVEN
    when(safetyClient.isSafe("safebar")).thenReturn(true);
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
//    Product product = productRepo.findById(id??) // #1 best
//    Product product = productRepo.findByName("name"); // #2 use anoteher finder
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);// #3 simply assuming the DB is empty initially

    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void missingCategoryDefaultsToUNCATEGORIZED() {
    when(safetyClient.isSafe("safebar")).thenReturn(true);
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    ProductDto dto = new ProductDto("name", "safebar", supplierId, null);

    productService.createProduct(dto);

    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);// #3 simply assuming the DB is empty initially
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }
}
