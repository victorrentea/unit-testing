package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;
@ActiveProfiles("test") // starts a H2 in-memory SQL DB
//  ❤️❤️❤️❤️❤️ much better🔽
//@SpringPostgresTestContainer // start a postgres in a docker for the duration of your tests
//@SpringMongoDBTestContainer

@EmbeddedKafka // boots up a Kafka broker emulator in my JUnit process ~ H2
//@SpringKafkaTestContainer
// or disable the rabbit/kafka consumers so you don't have to start a broker at all


//@ContextConfiguration 🤔 // boots selective class
//@WebMvcTest // boots only @RestControllers
//@WebFluxTest
@SpringBootTest // boots the entire app // ± on a base class
public class ProductServiceCreateTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockitoBean // replace the real bean in Spring context with a Mockito mock that you can when... and verify...
  // alternative: start a mockserver / wiremock emulating a reponse to your app's request
  SafetyApiClient safetyApiClient;
  @MockitoBean
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();

  @Test
  void createThrowsForUnsafeProduct() {
    productDto = productDto.withBarcode("barcode-unsafe");
    when(safetyApiClient.isSafe("barcode-unsafe")).thenReturn(false);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");
    when(safetyApiClient.isSafe("barcode-safe")).thenReturn(true);
//    when(productRepo.save(any())).thenReturn(new Product().setId(123L)); // emulate the behavior of Spring

    // WHEN
    var newProductId = productService.createProduct(productDto);

    // ask the mock the actual param value received in tested code 🤢🤮
//    ArgumentCaptor<Product> productCaptor = forClass(Product.class);
//    verify(productRepo).save(productCaptor.capture());
//    Product product = productCaptor.getValue();

    // ❤️
    var product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        assertArg(e-> assertThat(e.productId()).isEqualTo(newProductId)));
//    assertThat(product.getCreatedDate()).isToday(); // TODO can only integration-test as it requires Hibernate magic
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