package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.BaseIntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@AutoConfigureWireMock(port = 8089)
@TestPropertySource(properties = "safety.service.url.base=http://localhost:8089")
class CreateProductIntegrationTest extends BaseIntegrationTest {
  public static final String BARCODE = "barcode-safe";
  public static final String PRODUCT_NAME = "name";
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductService service;

  ProductDto dto = new ProductDto()
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE)
      .setName(PRODUCT_NAME)
      .setCategory(HOME);

  @Test
  void failsForUnsafeProduct() {
    // programmatic config of mock responses from
//    stubFor(get("/product/barcode-unsafe/safety")
//        .willReturn(okJson("""
//            {
//             "category": "NOT SAFE",
//             "detailsUrl": "http://details.url/a/b"
//            }
//            """)));
    dto.setBarcode("barcode-unsafe");

    assertThatThrownBy(() -> service.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void savesTheProduct() {
//    when(safetyApiClient.isSafe(BARCODE)).thenReturn(true);

    // prod call
    Long productId = service.createProduct(dto);

    Product product = productRepo.findById(productId).get();
    // SHOCK: this does not trigger an SELECT but retrieves product from JPA 1st level cache
    assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
    assertThat(product.getBarcode()).isEqualTo(BARCODE);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getSupplier().getCode()).isEqualTo(SUPPLIER_CODE);
    assertThat(product.getCreatedDate()).isToday();
  }

  @Test
  void sendsKafkaMessage() {
//    when(safetyApiClient.isSafe(BARCODE)).thenReturn(true);

    service.createProduct(dto);

    verify(kafkaTemplate).send(PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void defaultsCategoryToUncategorized() {
//    when(safetyApiClient.isSafe(BARCODE)).thenReturn(true);
    dto.setCategory(null);

    long productId = service.createProduct(dto);

    Product product = productRepo.findById(productId).get();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}