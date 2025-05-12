package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.lang.annotation.Retention;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
///  NEVER EVER EVER push on GIT without a link to a internal wiki page justifying
// in front of God Almighty why you decided do remove 30s of the life of all your colleagues at every build
// valid examples: add/remove singleton to sprign context, programatic change of config in spring, @Autoconfiguration < NO ONE does this.
@Retention(RUNTIME) // stops javac from removing it at compilation
@interface Fixed1yearOfFlakyTests {

}
@Fixed1yearOfFlakyTests
class BaseTest {}

@EmbeddedKafka
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWireMock(port = 0)
public class CreateProductBisTest extends BaseTest {
  @MockBean
  SupplierRepo supplierRepo;
  @MockBean
  ProductRepo productRepo;
  @MockBean // not in the other test classes => +1 context4322
  SafetyApiAdapter safetyApiAdapter;
  @MockBean
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;

  @Test
  void createThrowsForUnsafeProduct() {
    ProductDto productDto = new ProductDto("name", "barcode-unsafe", "S", HOME);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    stubFor(get(urlEqualTo("/product/barcode-safe/safety"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
            {
              "category": "SAFE",
              "detailsUrl": "http://details.url/a/b"
            }
        """)));

    when(supplierRepo.findByCode("S")).thenReturn(Optional.of(new Supplier().setCode("S")));
    when(productRepo.save(any())).thenReturn(new Product().setId(123L));
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    ArgumentCaptor<Product> productCaptor = forClass(Product.class);
    verify(productRepo).save(productCaptor.capture()); // as the mock the actual param value
    Product product = productCaptor.getValue();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        assertArg(e-> assertThat(e.productId()).isEqualTo(newProductId)));
  }

}