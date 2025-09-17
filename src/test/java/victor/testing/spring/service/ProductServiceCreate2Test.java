package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

@SpringBootTest//(properties = "unprop=altaval")❌
@ActiveProfiles("test")
//@ActiveProfiles({"test","siala"})// cere alt config ❌
@EmbeddedKafka
public class ProductServiceCreate2Test extends IntegrationTest {
  @Autowired // = @Mock+@Bean; adica inlocuieste beanul normal cu un mock configurabil
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @Autowired //singuru loc in care nu face @RAC
  ProductService productService;
  @MockitoSpyBean // ❌
  ProductMapper mapper;
  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();

  @BeforeEach
  @AfterEach
  final void before() { // #1 STERG inainte sa ma asez pe colac, da si dupa, sa-l las cum l-am gasit
    productRepo.deleteAll(); // cu grija in ordinea FKurilor
    supplierRepo.deleteAll();
    // cache.clear
    // singleton cu state.clear
    // kafka.drain topic pt 1 sec
  }

  @Test
  void createThrowsForUnsafeProduct() {
    productDto = productDto.withBarcode("barcode-unsafe");
    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).get();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        assertArg(e -> assertThat(e.productId()).isEqualTo(newProductId)));
    assertThat(product.getCreatedDate()).isToday(); // TODO can only integration-test as it requires Hibernate magic
  }

  @Test
  void createDefaultsMissingCategoryToUncategorized() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe")
        .withCategory(null);
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).get();
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