package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseIntegrationTest;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductService;
import victor.testing.spring.product.api.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;

//@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("db-mem")
//@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
//@TestPropertySource(properties = "safety.service.url.base=http://localhost:${wiremock.server.port}")
//@ActiveProfiles("wiremock")
@Transactional // se comporta altfel ca atunci cand il pui in prod: la final da rollback

//@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = BEFORE_TEST_METHOD)
// pt insert de date 'statice' standard in db gol, mai poti defini un fisier /src/test/resources/data.sql pe care Spring il ruleaza automat dupa creerea bazei

// NICIODATA pe git -> incetineste dramatic testele pe CI
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // DISTRUGE CONTEXTU DE SPRING CU TOT CU DB IN MEM CU TOT

//@AutoConfigureWireMock(port = 0) // porneste un server HTTP din teste
// port random, multi-thread ready
public class CreateProductTest extends BaseIntegrationTest {
//  @MockBean
//  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

//  @BeforeEach
//  @AfterEach
//  final void before() { // cleanup programatic din tabele in ordinea FK
//      productRepo.deleteAll();
//      supplierRepo.deleteAll();
//  }

  @Test
  void createThrowsForUnsafeProduct() {
//    when(safetyClient.isSafe("bar")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: bar");
  }

  @Test
  void createOk() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();
//    when(safetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }
  @Test
  void createOkCuCategoryNull() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();
//    when(safetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, null);

    // WHEN
    productService.createProduct(dto);

    // THEN
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
