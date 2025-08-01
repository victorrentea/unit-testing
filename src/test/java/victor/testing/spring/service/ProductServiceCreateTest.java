package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.SafetyApiWireMock;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

@Transactional
//@ActiveProfiles("test") // pt application-test.properties -> H2 in mem
//@SpringBootTest
//@EmbeddedKafka // kafka in mem JUnit
class ProductServiceCreateTest extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
// TODO scoti astea 2 linii de mai sus
//  + @EnableWireMock pe clasa de test pt a porni acest server HTTP in procesul JVM al JUnit
//  > copiind cod din SafetyApiWireMock faci toate testele sa treaca din nou

  @Autowired
  ProductService productService;

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();


  @Test
  void createThrowsForUnsafeProduct() {
    SafetyApiWireMock.stubResponse("barcode-unsafe","UNSAFE");
//    WireMock.stubFor(get(urlEqualTo("/product/barcode-unsafe/safety"))
//        .willReturn(okJson("""
//            {
//              "category": "UNSAFE",
//              "detailsUrl": "http://details.url/a/b"
//            }
//            """)));

    productDto = productDto.withBarcode("barcode-unsafe");
//    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    SafetyApiWireMock.stubResponse("barcode","SAFE");
    productDto = productDto.withBarcode("barcode");

    var newProductId = productService.createProduct(productDto);

    //then (assert)
    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
//    verify(kafkaTemplate).send(
//        eq(ProductService.PRODUCT_CREATED_TOPIC),
//        eq("k"),
//        assertArg(e-> assertThat(e.productId()).isEqualTo(newProductId)));
  }
  @Test
  void defaultsToUncategorizedWhenMissingCategory() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe").withCategory(null);
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}