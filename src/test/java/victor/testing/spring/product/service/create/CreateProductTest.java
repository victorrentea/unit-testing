package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;

//@SpringBootTest
//@ActiveProfiles("db-mem")
// - JdbcTemplate cu SQL nativ poate folosi sintaxa/functii de Ora care NU sunt pe H2
// eg CONNECT BY, /*+hinturi*/
// daca vrei sa testezi chstii specifice ORA: 1) TEST_DB 2) Testcontainers🐳


////@Sql(scripts = "classpath:/sql/cleanup.sql") // #2
////@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // #3 = +30..60s / @Test pe CI/CD


//@Transactional // #4 👑 daca il pui in teste, spring da ROLLBACK automat dupar fiecare @Test
// slabiciuni:
// NU MERGE DACA codul testat face @Async/CompletableFuture/THreadPool sau @Transactional(propagation = Propagation.REQUIRES_NEW)
// POATE SA NU VADA BUGURI pt ca nu se intampla niciodata COMMIT in DB

@ActiveProfiles("sieu")
@TestPropertySource(properties = "safety.service.url.base=http://localhost:${wiremock.server.port}")
@AutoConfigureWireMock(port = 0) // publica in Spring o proprietate numita ${wiremock.server.port}
public class CreateProductTest extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
//  @MockBean
//  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductService productService;

  // #1 manual cleanup de DB: ideal pt db non-sql
//  @BeforeEach
//  @AfterEach
//  final void before() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();
//  }
  @Test
  void createThrowsForUnsafeProduct() {
   // java stubbing
    stubFor(get("/product/unsafe/safety").willReturn(okJson(
        """
                {
              "entries": [
                {"io": 2,
                  "category": "DETERMINED",
                  "detailsUrl": "http://details.url/a/b"
                }
              ]
            }
            """)));
//    when(safetyClient.isSafe("unsafe"))
//        .thenReturn(false);
    ProductDto dto = new ProductDto("name", "unsafe", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: unsafe");
  }

  @Test
  @CuUserNormal
  void createOk() {
    // src/test/resources/mappings/*.json sunt citite automat
    ProductDto dto = new ProductDto("name", "safe", supplierId, HOME);

    productService.createProduct(dto);

    Product product = productRepo.findByName("name");
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getSku()).isEqualTo("safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic
    assertThat(product.getCreatedBy()).isEqualTo("jdoe"); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void defaultToUncategorizedWhenMIssingCategory() {
//    when(safetyClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safe", supplierId, null);

    productService.createProduct(dto);

    Product product = productRepo.findByName("name");
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
