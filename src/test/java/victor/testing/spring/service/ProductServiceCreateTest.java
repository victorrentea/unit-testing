package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.inject.Inject;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.wiremock.spring.EnableWireMock;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

// NICIODATA PE GIT SA NU APARA:
//@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
// restarteaza Springu(10-15s) dupa fiecare @Test // fura luni din viata colegilor / agentilor; lungeste buildu
// distruge H2 in mem

@Transactional // #1 face ROLLBACK automat pe @Test❤️❤️❤️
  // 🙁 @Transactional(REQ_NEW/NOT_SUPP), @Asymc
  // 🙁 pt ca nu se face COMMIT, unele checkuri ai putea sa nu le atingi

//@Sql(value = "classpath:/sql/cleanup.sql",executionPhase = BEFORE_TEST_METHOD) // #3
public class ProductServiceCreateTest
      extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @Inject
  ProductService productService;

//  @AfterEach // #
//  @BeforeEach
  public void cleanup() { // curatare responsabila
    productRepo.deleteAll();
    supplierRepo.deleteAll();
    // cache.clear
    // kafka.drain
    // mongo.delete
  }

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();

  @Test
  void createThrowsForUnsafeProduct() {
    stubFor(get(urlEqualTo("/product/barcode-unsafe-dsl/safety"))
        .willReturn(okJson("""
            {
              "detailsUrl": "http://details.url/a/b",
              "category": "%s"
            }
            """.formatted("UNSAFE"))));

    productDto = productDto.withBarcode("barcode-unsafe-dsl");

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");

    WireMock.verify(getRequestedFor(urlEqualTo("/product/barcode-unsafe-dsl/safety")));
  }

  @Test
  @WithMockUser(username = "john")
  void createOk() throws InterruptedException, TimeoutException {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreatedBy()).isEqualTo("john"); // TODO test framework magic
    assertThat(product.getCreatedDate()).isToday();
    var recordReceivedFromKafka =
        productCreatedEventTestListener.blockingReceive(Duration.ofSeconds(1));
    assertThat(recordReceivedFromKafka.value().productId()).isEqualTo(newProductId);
    assertThat(recordReceivedFromKafka.key()).isEqualTo("key");
  }

  @Test
  @WithMockUser(username = "john")
  void createOkWithoutCategory() throws InterruptedException, TimeoutException {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe").withCategory(null);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED); // 👍
    productCreatedEventTestListener.blockingReceive(Duration.ofSeconds(2));
    // sa drenez coada. sa nu las mesaje neconsumate
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