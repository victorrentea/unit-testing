package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

@SpringBootTest
@EmbeddedKafka
@ActiveProfiles("test")
@Transactional

public class ProductServiceCreate2Test {
  @Autowired // imi ❤️ baza, o vreau reala
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockitoBean
  SafetyApiAdapter safetyApiAdapter;
  @MockitoBean // inlocuieste un bean din context cu un mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;
//  @BeforeEach // curatare #1
//  final void before() { // merge mereu pe orice ai de curatat
//    //  Mongo, Redis, golit cozi prin pub-sub, curatat cacheuri, fisiere
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();// in ordine 😱
//    // sa nu uit vreo tabela😱
//  }

  ProductDto productDto = ProductDto.builder()
          .name("name")
          .supplierCode("S")
          .category(HOME)
          .build();

  @Test
  void createThrowsForUnsafeProduct() {
    productDto = productDto.withBarcode("barcode-unsafe");
    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);

    assertThatThrownBy(() -> productService.createProduct(productDto))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Product is not safe!");
  }

  @Test
  @WithMockUser(username = "schusterl")
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
            eq(ProductService.PRODUCT_CREATED_TOPIC),
            eq("k"),
            assertArg(e -> assertThat(e.productId()).isEqualTo(newProductId)));
    assertThat(product.getCreatedBy()).isEqualTo("schusterl");
    assertThat(product.getCreatedDate()).isToday(); // TODO can only integration-test as it requires Hibernate magic
//    assertEquals// interzis prin lege!❌❌❌
//    assertThat(product.getCreatedDate()).isCloseTo(
//        LocalDateTime.now(),byLessThan(500, ChronoUnit.MILLIS)); // TODO can only integration-test as it requires Hibernate magic
  }

  @Test
  void shouldDefaultToUncategorized_forMissingCategory() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe").withCategory(null);
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

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