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
import static java.util.concurrent.TimeUnit.SECONDS;
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

//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)//cea mai proasta idee dar e Vineri
// o idee proasta pt ca adauga +10..60 secunde / @Test #4 nu o face
@ActiveProfiles("test")
@EmbeddedKafka(topics = {SUPPLIER_CREATED_EVENT, PRODUCT_CREATED_TOPIC})
@SpringBootTest // porneste app spring in memoria JUnit
//@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = BEFORE_TEST_METHOD) #3
@Transactional // daca pui in teste, face rollback la sfarsitul fiecarui test 💖 #1
public class CreateProductTest /*extends IntegrationTest*/ {
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

  @RegisterExtension
  static WireMockExtension wireMockServer = WireMockExtension.newInstance()
      .options(options().port(9999)) // TODO sa fie dynamicPort sa nu te calci pe porturi cu alte procese
      .build();
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

    var newProductId = productService.createProduct(productDto);

    LocalDateTime startTime = now();
    var record = testListener.blockingReceive(
        r->true, Duration.ofSeconds(1));
    assertThat(record.value().productId()).isEqualTo(newProductId);
//    assertThat(record.value().observedAt()).isEqualTo(LocalDateTime.now());// pica
//    assertThat(record.value().observedAt().truncatLaSec).isEqualTo(LocalDateTime.now());// flaky
//    assertThat(record.value().observedAt()).isNotNull();// prea de jr
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

  @Autowired
  protected ProductCreatedEventTestListener testListener;

  @TestConfiguration // adaug la @Configuration pentru a nu fi incarcat in productie
  public static class KafkaTestConfig {
    @Bean
    public ProductCreatedEventTestListener productCreatedEventTestListener() {
      return new ProductCreatedEventTestListener();
    }
  }

  @Slf4j
  public static class ProductCreatedEventTestListener {
    private LinkedBlockingQueue<ConsumerRecord<String, ProductCreatedEvent>> receivedRecords = new LinkedBlockingQueue<>();

    @PostConstruct
    void init() {
      log.info("Test listener initialized");
    }
    @KafkaListener(topics = PRODUCT_CREATED_TOPIC)
    void receive(ConsumerRecord<String, ProductCreatedEvent> record) {
      log.debug("Test listener received message: {}", record);
      receivedRecords.add(record);
    }

    public ConsumerRecord<String, ProductCreatedEvent> blockingReceiveForHeader(
        String headerKey, String headerValue, Duration timeout) throws ExecutionException, InterruptedException, TimeoutException {

      return blockingReceive(record -> filterByHeader(headerKey, headerValue, record), timeout);
    }

    private boolean filterByHeader(String headerKey, String headerValue, ConsumerRecord<String, ProductCreatedEvent> record) {
      Header header = record.headers().lastHeader(headerKey);
      if (header == null) return false;
      return headerValue.equals(new String(header.value()));
    }

    // drop all until I find one that matches the selector or timeout occurs
    // Challenge: uniquely identify the message expected (eg: use an UUID)
    public ConsumerRecord<String, ProductCreatedEvent> blockingReceive(
        Predicate<ConsumerRecord<String, ProductCreatedEvent>> messageSelector, Duration timeout) throws ExecutionException, InterruptedException, TimeoutException {
      LocalDateTime deadline = now().plus(timeout);
      while (true) {
        Duration timeLeft = Duration.between(now(), deadline);
        var record = receivedRecords.poll(timeLeft.toMillis(), MILLISECONDS);
        if (record == null) {
          throw new TimeoutException("Timeout while waiting for message");
        }
        if (messageSelector.test(record)) {
          log.info("Received message matched test: {}", record);
          return record;
        }else {
          log.info("Discarding message not matching test: {}", record);
        }
      }
    }
  }

}