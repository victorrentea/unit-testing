package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.tools.TruncateTables;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;
import static victor.testing.tools.ClockTestUtils.fixedDateClock;

//@ActiveProfiles({"test"/*,"second"❌*/}) // starts a H2 in-memory SQL DB
//  ❤️❤️❤️❤️❤️ much better🔽
//@SpringPostgresTestContainer // start a postgres in a docker for the duration of your tests
//@SpringMongoDBTestContainer
//@EmbeddedKafka//❌ // boots up a Kafka broker emulator in my JUnit process ~ H2
//@SpringKafkaTestContainer
// or disable the rabbit/kafka consumers so you don't have to start a broker at all


//@ContextConfiguration 🤔 // boots selective class
//@WebMvcTest // boots only @RestControllers
//@WebFluxTest

//@ClearTables ❤️picnic solution
//@SpringBootTest//❌(properties = "some.prop=different value") // boots the entire app // ± on a base class
@TruncateTables({"product", "supplier"})
public class ProductServiceCreate2Test extends IntegrationTest  {
  public static final UUID UUID = java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
  @Autowired
  SupplierRepo supplierRepo;
//  @Autowired // WRAPS⭐️ the real productRepo bean with a Mockito mock that you can when... and verify...
//  ProductRepo productRepo;
  @MockitoBean // replaces the real bean in Spring context with a Mockito mock that you can when... and verify...
  // alternative: start a mockserver / wiremock emulating a reponse to your app's request
  SafetyApiClient safetyApiClient;
  @MockitoBean
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;
  @MockitoBean
  java.util.function.Supplier<UUID> uuidSupplier;

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
  @WithMockUser(username = "userx") // set up a SecurityContext
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");
    when(safetyApiClient.isSafe("barcode-safe")).thenReturn(true);
    when(uuidSupplier.get()).thenReturn(UUID);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    // #2: ask the Mockito spy wrapping the real repo 🤢🤮
//    ArgumentCaptor<Product> productCaptor = forClass(Product.class);
//    verify(productRepo).save(productCaptor.capture());
//    Product product = productCaptor.getValue();

    // #1 ❤️ less mocking and closer to reality: SELECT from a DB
    var product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        assertArg(e-> {
          assertThat(e.someUuid()).isEqualTo(UUID);
          assertThat(e.productId()).isEqualTo(newProductId);
        }));
    assertThat(product.getCreatedBy()).isEqualTo("userx");// testing framework magic
    // TODO assert that product.createdDate is now
//    assertThat(product.getCreatedDate()).isEqualTo(LocalDateTime.now()); //❌ fails due to few ms skew
    //✅ #1 works despite the few ms skew
//    assertThat(product.getCreatedDate()).isCloseTo(LocalDateTime.now(), byLessThan(ofSeconds(1)));
    //✅ #2 inject in prod a mock Clock
    assertThat(product.getCreatedDate()).isEqualTo(LocalDateTime.of(2025, 12, 25, 0, 0));
    //✅ #3 define a new CLock bean fixed on a moment in time, and  have it replace the clock bean in the actual real production

//    assertThat(product.getSomeUuid()).isNotNull(); // may be good enough
    assertThat(product.getSomeUuid()).isEqualTo(UUID);
  }

  @Test
//  void createOkUncategorized() {
//  void withCategoryNull() {
//  void withMissingCategory() {
  void defaultsToUncategorizedWhenMissingCategory() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe").withCategory(null);
    when(safetyApiClient.isSafe("barcode-safe")).thenReturn(true);

    var newProductId = productService.createProduct(productDto);

    var product = productRepo.findById(newProductId).orElseThrow();
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