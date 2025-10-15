package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

//@SpringBootTest
//@ActiveProfiles("test")
//@EmbeddedKafka

@Transactional // in src/test spring da rollback la final dupa fiecare @Test. in aceasta TX
// toate @BeforeEach/After ruleaza (ale tale sau mostenite)
// nu merge daca codul tau face @Async sau propagatipn=REQUIRES_NEW/NOT_SUPPORTED
// nu joaca @TransactionalEventListener(AFTER_COMMIT)
// posibil sa nu faca UPDATE/INSERT in DB ca tot asteapta commit

//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)// SA NU TE PRIND CU EL IN PROIECT, ca vin la tine!
// ai furat din viata tuturor colegilor care se uita acum la CI cum ruleaza testele 40 min
// curata starea din DB din memorie pt ca moare spring cu tot cu H2
// daca DBul tau e intrun Docker testcontainered => NU VA MERGE!
@Slf4j
public class ProductServiceCreateTest extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  ProductService productService;

  @BeforeEach
  final void before() throws ExecutionException, InterruptedException, TimeoutException {
    try {
      productCreatedEventTestListener // schita:
          .blockingReceive(r -> true,
              Duration.ofSeconds(1)); // FIXME evita
    } catch (Exception e) {
      log.trace("Ignor un timeout care inseamna ca nu a fost nimic pe teaza de curatat, in topicul de drenat.", e);
    }
  }

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();

  @Test
  void createThrowsForUnsafeProduct() {
    productDto = productDto.withBarcode("barcode-uxynsafe");
//    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);
    WireMock.stubFor(
        get(
            urlEqualTo("/product/barcode-uxynsafe/safety"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                            {
                              "detailsUrl": "http://details.url/a/b",
                              "category": "UNSAFE"
                            }
                        """))
    );


    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  @WithMockUser(username = "pink")
  void createOk() throws ExecutionException, InterruptedException, TimeoutException {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    var rec = productCreatedEventTestListener.
        blockingReceive(r -> true,
            Duration.ofSeconds(3));
    ProductCreatedEvent event = rec.value();
    assertThat(event.productId()).isEqualTo(newProductId);
    assertThat(product.getCreatedDate()).isToday(); // TODO can only integration-test as it requires Hibernate magic
    assertThat(product.getCreatedBy()).isEqualTo("pink");
  }

  @Test
  void createProductWithoutCategory() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe").withCategory(null);
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

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