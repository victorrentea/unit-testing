package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;
import static victor.testing.spring.listener.MessageListener.SUPPLIER_CREATED_EVENT;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

public class CreateProductTest extends ITest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
//  @MockBean
//  SafetyApiAdapter safetyApiAdapter;
//  @MockBean // replaces the real bean instance witha mockito mock
//  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;


//  @BeforeEach // solutie foarte buna #2
//  @AfterEach
//  final void cleanup() {
//      productRepo.deleteAll();
//      supplierRepo.deleteAll();
//  }

  @BeforeEach
  final void before() {
    supplierRepo.save(new Supplier().setCode("S"));
  }
  @Test
  void createThrowsForUnsafeProduct() {
//    wireMockServer.stubFor(get(urlEqualTo("/product/barcode-safe/safety"))
//        .willReturn(aResponse()
//            .withHeader("Content-Type", "application/json")
//            .withBody("""
//                {
//                  "category": "UNSAFE",
//                  "detailsUrl": "http://details.url/a/b"
//                }
//                """)));
    ProductDto productDto = new ProductDto("name", "barcode-unsafe", "S", HOME);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).get();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
  }
  /// ✄ -------- taie aici testul
  @Test
  void kafkaMessageSent() throws ExecutionException, InterruptedException, TimeoutException {

    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);
    String trace = "IO" + UUID.randomUUID().toString();
    MDC.put("traceid", trace);

    var newProductId = productService.createProduct(productDto);

    LocalDateTime startTime = now();
    var record = testListener.blockingReceive(
        r-> {
          Header header = r.headers().headers("traceid").iterator().next();
          String value = new String(header.value());
          return trace.equals(value);
        }, Duration.ofSeconds(1));
    assertThat(record.value().productId()).isEqualTo(newProductId);
    assertThat(record.value().observedAt()).isCloseTo(startTime, byLessThan(1, ChronoUnit.SECONDS));
  }

  @Test
  void defaultsToUncategorizedForMissingCategory() {
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", null);

    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).get();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}