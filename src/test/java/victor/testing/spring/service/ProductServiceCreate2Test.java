package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

//@SpringBootTest
//@ActiveProfiles("test")// H2 = sql in-mem
//@EmbeddedKafka // Kafka broker in-mem
//@Transactional //  initiaza o tx inca de la inceputul
// @Test pe care o propaga in codul testat; dupa @Test i se rollback.
// Nu merge daca codul testat...
// a) @Transactional(propagation=REQUIRES_NEW)
// b) porneste threaduri noi / @Async / CompletableFuture @victor
//@Sql(...)

//@TPS
public class ProductServiceCreate2Test extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;

  @MockitoBean // inlocuieste beanul real cu un mock Mockito (pe care poti sa when/verify)
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;

  // === testele de integrare
  // 1) pot crapa pt ca un test precedent a lasat date in DB
  // a) @Transactional pe clasa de test => auto-rollback dupa fiecare @Test
  // b) repo.deleteAll() in afterEach+beforeEach
  // c) @Sql(..."cleanup.sql") - cand ai >100 tabele ± pl/sql,native query
  // d) @DirtiesContext -> NICIODATA!!😡🤬🤛
  @BeforeEach
  @AfterEach
  final void before() {
      productRepo.deleteAll();
      supplierRepo.deleteAll();
  }

  // 2) sunt lente

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();

  @Test
  void createThrowsForUnsafeProduct() {
    productDto = productDto.withBarcode("barcode-unsafeX");
//    when(safetyApiAdapter.isSafe("barcode-unsafeX")).thenReturn(false);
    doReturn(false).when(safetyApiAdapter).isSafe("barcode-unsafeX");

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safeX");
//    when(safetyApiAdapter.isSafe("barcode-safeX")).thenReturn(true);
    doReturn(true).when(safetyApiAdapter).isSafe("barcode-safeX");
//    doReturn(true).when(safetyApiAdapter).isSafe("barcode-XXXXXXXX");

    // WHEN
    var newProductId = productService.createProduct(productDto);

    verify(safetyApiAdapter).isSafe(anyString());// ca exact 1 data!
    // Mockito by default tipa daca faci un when-return fara sa fie folosit in codul testat " UnnecessaryStubbing
    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safeX");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        assertArg(e -> assertThat(e.productId()).isEqualTo(newProductId)));
//    assertThat(product.getCreatedDate()).isToday(); // TODO can only integration-test as it requires Hibernate magic
  }

  @Test
  void createOk_withCategoryNull() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safeX").withCategory(null);
//    when(safetyApiAdapter.isSafe("barcode-safeX")).thenReturn(true);
    doReturn(true).when(safetyApiAdapter).isSafe("barcode-safeX");

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}

// region WireMock
// 1. TODO add @EnableWireMock => tests ✅
// 2. edit the dto.barcode => tests ❌ => TODO locate the *.json to fix to pass tests ✅
// 3. change name of folder 'mappings' from /src/test/resources/ => TODO fix by usin Java DSL like:
//   WireMock.stubFor(get(urlEqualTo("/url"))
//       .willReturn(okJson("""
//        {
//         "p1": "v1"
//        }
//        """)));
// endregion