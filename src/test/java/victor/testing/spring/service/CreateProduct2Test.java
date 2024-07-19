package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

// 💖
@Transactional //on test classes is special: it tells spring not to commit at the ned
// but rollback all inserts done by @Test, @BeforeEach [inerited]

// magic with 1 conditnion: you don't change the thread or @Transactional(REQUIRES_NEW|NOT_SUPPORTED)

// #2
//@Sql(scripts = "classpath:/sql/cleanup.sql",executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)

// NEVER* PUT ON GIT!=> spring is slwo
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
public class CreateProduct2Test extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockBean
  SafetyApiAdapter safetyApiAdapter;
  @MockBean
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;
  private ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);

//  @BeforeEach // #1 for small, decent db with JPA on top
//  @AfterEach // so you don't sh*t on other naive tests after you
//  final void setup() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();
//  }

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);
    ProductDto productDto = new ProductDto("name", "barcode-unsafe", "S", HOME);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void happy() {
    Long supplierId = supplierRepo.save(new Supplier().setCode("S")).getId();
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    // WHEN
    Long id = productService.createProduct(productDto);

    Product product = productRepo.findById(id).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        argThat(e -> e.productId().equals(id)));
//    productRepo.deleteById(id);
//    supplierRepo.deleteById(supplierId);
  }

  @Test
  void defaultToUncategorized() {
    supplierRepo.save(new Supplier().setCode("S"));
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
    productDto.setCategory(null);

    // WHEN
    Long id = productService.createProduct(productDto);

    Product product = productRepo.findById(id).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}