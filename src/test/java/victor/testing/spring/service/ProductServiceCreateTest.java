package victor.testing.spring.service;

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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.wiremock.spring.EnableWireMock;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

//@SpringBootTest
//@EmbeddedKafka // in-mem
//@ActiveProfiles("test")

//@Sql(value = "/sql/cleanup.sql",executionPhase = BEFORE_TEST_METHOD) // curatare #3 (inainte de fiecare @Test)
// recomandat cand testezi SQLuri multe / proceduri stocate

//❤️ Note: 999_insert_test_data.sql se executa automat pt profile 'test' la pornirea contextului= date de referinta

//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)// ❌❌❌#4 NICIODATA, dar AI/SO o mai sugereaza
// distruge Springu si H2 din el pt fiecare test, platesti 10-30s/@Test
// PRINCIPALA GAFA in @SpringBootTest
// => "aaaa, da-le-ncolo de teste cu spring, ca-s lente. Nu mai bine dam ClickDr>Copilot>generate teste
// stiu ca-s microscopice si fragile, si scoase din ctx de biz, da-mi da covrigi mult

//@EnableWireMock// simuleaza un servicu extern porning un server HTTP in-mem
// -MockServer
// -simulatoare stateful pe care noi le scriem (+🐞)
//    doar pt testele noastre, reimplementand ce fac aia (dac-am inteles)
// 🙁 nu merge daca codul testat face @Transactional(propagation=REQUIRES_NEW, sau multithread
// 😱 nu acopera commit, @TransactionalEventListener(AFTER_COMMIT)r
@Transactional //  curatare #2 ❤️❤️❤️ (src/test -> rollback dupa fiecare @Test)
public class ProductServiceCreateTest extends IntegrationTest {
  @Autowired // imi ❤️ baza, o vreau reala
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockitoBean // inlocuieste un bean din context cu un mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;
//  @BeforeEach // curatare #1
//  final void before() { // merge mereu pe orice ai de curatat
//    //  Mongo, Redis, golit cozi prin pub-sub, curatat cacheuri, fisiere
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();// in ordine 😱
//    // sa nu uit vreo tabela😱
//  }

  ProductDto productDto = ProductDto.builder()
          .name("name")
          .supplierCode("S")
          .category(HOME)
          .build();

  @Test
  void createThrowsForUnsafeProduct() {
    stubFor(get(urlEqualTo("/product/barcode-unsafeY/safety"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    //language=json
                    .withBody("""
                            {
                              "detailsUrl":"http://details.url/a/b",
                              "category":"UNSAFE"
                            }
                            """)));
    productDto = productDto.withBarcode("barcode-unsafeY");

    assertThatThrownBy(() -> productService.createProduct(productDto))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Product is not safe!");
  }

  @Test
  @WithMockUser(username = "schusterl")
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
            eq(ProductService.PRODUCT_CREATED_TOPIC),
            eq("k"),
            assertArg(e -> assertThat(e.productId()).isEqualTo(newProductId)));
    assertThat(product.getCreatedBy()).isEqualTo("schusterl");
    assertThat(product.getCreatedDate()).isToday(); // TODO can only integration-test as it requires Hibernate magic
//    assertEquals// interzis prin lege!❌❌❌
//    assertThat(product.getCreatedDate()).isCloseTo(
//        LocalDateTime.now(),byLessThan(500, ChronoUnit.MILLIS)); // TODO can only integration-test as it requires Hibernate magic
  }

  @Test
  void shouldDefaultToUncategorized_forMissingCategory() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe").withCategory(null);
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

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