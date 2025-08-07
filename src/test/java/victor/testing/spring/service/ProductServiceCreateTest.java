package victor.testing.spring.service;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.tools.CaptureSystemOutput;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka // a kind of H2
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // NEVER ON GIT!
  // if db in testcontainer, it will survive SPring's death anyway

// #2 for XXL sql database
//@Sql(scripts = "classpath:/sql/cleanup.sql",executionPhase = BEFORE_TEST_METHOD)

//  #3
@Transactional // start each @Test in a new tx on the current thread, then after the @Test ROLLBACK it
  // the @Test propagates this "test roolback-only transaction" into the tested prod code
// PITFALLS: XXX some prod habits might stop test @Transactions
//
class ProductServiceCreateTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockitoBean // replaces the real bean with a mockito mock
  SafetyApiAdapter safetyApiAdapter;
  @MockitoBean
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();

  @BeforeEach
  final void before() {
//    productRepo.deleteAll();// #1
//    supplierRepo.deleteAll();
    supplierRepo.save(new Supplier().setCode("S"));
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
  }

//  @AfterEach // safest and most general purpose
//  final void cleanupAfter() {
//    productRepo.deleteAll(); // in order
//    supplierRepo.deleteAll();
//    // mongo detelall
//    // redis clear cache
//    // kafka drain pending messages
//  }

  @Test
  @CaptureSystemOutput
  void createThrowsForUnsafeProduct(CaptureSystemOutput.OutputCapture outputCapture) {
    productDto = productDto.withBarcode("barcode-unsafe");
    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
    assertThat(outputCapture.toString()).contains("[ALARM-CALL-LEGAL]");

  }

  @Test
  void savesProductToDB() {
    productDto = productDto.withBarcode("barcode-safe");

    // WHEN
    Long productId = productService.createProduct(productDto);
    productRepo.flush(); // does NOT commit yet. still rollback at end
    System.out.println("prod finished");

    // there is no insert yet because there was no COMMIT to flush the write buffer of JPA
    //  entity is in Hibernates' 1st level cache(PersistenceContext)
    // productRepo.findById returns from the 1st level cache since "by PK
    // supllierRepo.findByCode is translated to SQL and ran to DB, Hib has to pre-flush the CHANGES
    // since there was no INSERT / commit in the game, the UQ/NOT NULL, FKs were not checked YET by DB
    // Also, the @TransactionalEventListener(POST_COMMIT) never ran
    Product product = productRepo.findById(productId).orElseThrow();
    assertThat(product)
        .returns("name", Product::getName)
        .returns("barcode-safe", Product::getBarcode)
        .returns("S", p -> p.getSupplier().getCode())
        .returns(HOME, Product::getCategory);
    assertThat(product.getCreatedDate()).isToday(); // spring+jpa magic
  }
  @Test
  void kafkaEventIsSent() {
    productDto = productDto.withBarcode("barcode-safe");

    // WHEN
    Long productId = productService.createProduct(productDto);

    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC), // Pro: syntax reference
        eq("k"),
//        argThat(event -> event.productId().equals(productId)) // if only one attr is interesting
        productCreatedEventCaptor.capture()
    );
    ProductCreatedEvent event = productCreatedEventCaptor.getValue();
    assertThat(event.productId()).isEqualTo(productId);
    assertThat(event.observedAt()).isCloseTo(now(), byLessThan(1, MINUTES));
  }

  @Test
  void defaultsMissingCategoryToUncategorized() {
    productDto = productDto.withBarcode("barcode-safe")
        .withCategory(null);

    // WHEN
    Long productId = productService.createProduct(productDto);

    Product product = productRepo.findById(productId).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }
  @Captor
  ArgumentCaptor<ProductCreatedEvent> productCreatedEventCaptor;
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