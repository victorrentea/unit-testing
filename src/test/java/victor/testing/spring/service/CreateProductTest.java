package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;


//@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD) // #2 dupa fiecare @Test distruge Springu cu tot cu DB H2 in-mem
// ⚠️ distruge performanta - testele ruleaza muuuuuuuuuuuuuuuuuuuuuuuuuuuuuuult
// e de folosit doar ca 1) debug tool sau 2) cand testezi extensii/@ConditionalOn....ceva care modifica BEANUrile

// pentru cei care in tacere sufera eroic cu o DB legacy de 667 de
// tabele si muuuulst SuQiLi mostenit de la parintii firmei. de obicei ai si 50k de linii de PL/SQL
// nu ai JPA
//@Sql(scripts = "classpath:sql/cleanup.sql",executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)

@Transactional // #4 pe teste se face rollback automat la final
// + foate simplu : se curata magic tot dupa tine si @Before-urile dinainte ta
// - nu se face commit niciodata => @TransactionalEventListener nu merge
// - nu merge daca codul testat face @Transactional(propagation = REQUIRES_NEW|NOT_SUPPORTED) << DE EVITAT

@WithMockUser(username = "user", roles = "ADMIN")
@AutoConfigureWireMock(port = 0) // #3 porneste un server WireMock pe un port random
public class CreateProductTest extends IntegrationTest {
  @Autowired
  ProductService productService;

  @Autowired
  protected ProductRepo productRepo;

//  @BeforeEach // #1 repo.deleteAll f buna pt JPA
//  @AfterEach
//  public void cleanupDB() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();
//  }

  //  @Autowired
//  WireMock wireMock;
  @Autowired
  WireMockServer wireMockServer;

  @Test
  void createThrowsForUnsafeProduct() {
    wireMockServer.stubFor(get("/product/upc-unZZZ/safety")
        .willReturn(okJson("""
            {
             "category": "NOT SAFE",
             "detailsUrl": "http://details.url/a/b"
            }
            """)));

    ProductDto dto = new ProductDto("name", "upc-unZZZ", "S", HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
//    when(safetyClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "upc-safe", "S", HOME);

    // WHEN
    productService.createProduct(dto);

    Product product = productRepo.findByName("name");
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getUpc()).isEqualTo("upc-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("s");
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void createUncategorized() {
//    when(safetyClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name",
        "upc-safe", "S", null);

    // WHEN
    productService.createProduct(dto);

    Product product = productRepo.findByName("name");
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }


}
