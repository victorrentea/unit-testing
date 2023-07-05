package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseDatabaseTest;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;
// am uitat sa rulez TOATE TESTELE DECENTE INAINTE DE COMIT
//@Tag("slow")
//@SpringBootTest // porneste spring context
//@ActiveProfiles("db-mem")

//@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //

// Nukes Spring // cel mai antisocial lucru pe care-l poti face in test integrare:
// fiecare @Test adauga 30s la Jenkins build, ca reporneste spring cu tabelem ,,, hibern ,,,
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

@Transactional // in teste porneste o tranzactie separata / @Test, care dupa test este ROLLBACKed
// in acea tx ruleaza toate @Sql @BeforeEach (din super clase)
// + nu mai ai nevoie deloc de cleanup
// - nu merge daca codul testat nu lasa tranzactia de test sa intre:
//    - propagation=REQUIRES_NEW/NOT_SUPPORTED
//    - schimba threadul: @Async, executori, CompletableFuture, trimiti mesaj peste MQ
//  ==> N-ai ce sa faci decat sa renunti la @Transactional de pe test -> @BeforeEach cleanup
// - pericole: poti rata buguri: citeste : https://dev.to/henrykeys/don-t-use-transactional-in-tests-40eb

@AutoConfigureWireMock(port = 0) // ridica un server HTTP pe localhost pe port random
// care raspunde automat pe baza fisierelor by default in /src/test/resources/mappings/*.json
@TestPropertySource(properties = "safety.service.url.base=http://localhost:${wiremock.server.port}")
public class ProductServiceCreateBisTest extends BaseDatabaseTest {

  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

//  @AfterEach// nu e sufient
//  @BeforeEach // asa da
//  final void before() {
//    // in ordinea FK domle!
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();
//  }

  @Test
  void throwsForUnsafeProduct() {
//    when(safetyClient.isSafe("bar")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: bar");
  }

  @Test
  void ok() {
//    when(safetyClient.isSafe("safebar")).thenReturn(true);
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    productService.createProduct(dto);

    // FIND BY UNIQUE CRITERIA⭐️
//    Product product = productRepo.findByName("name"); // find in DB dupa 1 criteriu
//    Product product = productRepo.findById("name"); // find in DB dupa 1 criteriu

    // PRESUPUNAND CA BAZA INITIAL A FOST GOALA
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
     assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void missingCategoryDefaultsToUNCATEGORIZED() {
//    when(safetyClient.isSafe("safebar")).thenReturn(true);
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    ProductDto dto = new ProductDto("name",
        "safebar", supplierId, null);

    /*productId=*/productService.createProduct(dto);

//    assertThat(productRepo.findAll()).hasSize(1);
//    Product product = productRepo.findAll().get(0);
    Product product = productRepo.findByName("name");
    // cel mai sfant era dupa ID-ul nou atribuit din prod asa:
//    Product product = productRepo.findById(productId).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
