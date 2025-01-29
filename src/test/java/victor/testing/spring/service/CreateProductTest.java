package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
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

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)//cea mai proasta idee dar e Vineri
// o idee proasta pt ca adauga +10..60 secunde / @Test #4 nu o face
@ActiveProfiles("test")
@EmbeddedKafka
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
  @MockBean // replaces the real bean instance witha mockito mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
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
    supplierRepo.save(new Supplier().setCode("S"));
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
//    when(productRepo.save(any())).thenReturn(new Product().setId(123L));
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).get();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    /// ✄ -------- taie aici testul
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        assertArg(e-> assertThat(e.productId()).isEqualTo(newProductId)));
  }

  @Test
  void defaultsToUncategorizedForMissingCategory() {
    supplierRepo.save(new Supplier().setCode("S"));
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", null);

    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).get();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}