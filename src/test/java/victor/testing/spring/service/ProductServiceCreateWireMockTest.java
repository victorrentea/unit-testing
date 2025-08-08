package victor.testing.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.wiremock.spring.EnableWireMock;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.infra.SafetyApiAdapter.SafetyResponse;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.tools.CaptureSystemOutput;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
@Transactional
@EnableWireMock // !! this avoids restarting wiremock between test classes
  // default port = 0 => random to avoid port collisions on CI
class ProductServiceCreateWireMockTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
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
    supplierRepo.save(new Supplier().setCode("S"));
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
  }

  @Test
  @CaptureSystemOutput
  void createThrowsForUnsafeProduct(CaptureSystemOutput.OutputCapture outputCapture) throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
    productDto = productDto.withBarcode("barcode-unsafe");
    ObjectMapper jackson = new ObjectMapper();
    stubFor(get(urlEqualTo("/product/%s/safety".formatted(productDto.barcode())))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
//            .withBody("""
//            {
//              "detailsUrl": "http://details.url/a/b",
//              "category": "UNSAFE"
//            }
//        """)
            // + Java-only, no json string
            // - does NOT cover match between actual JSON and Their DTO in my code
            //    eg: I modeled int in their DTO, but they send me "13"
            .withBody(jackson.writeValueAsString(new SafetyResponse(
                "UNSAFE", "http://details.url/a/b")))
        ));

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