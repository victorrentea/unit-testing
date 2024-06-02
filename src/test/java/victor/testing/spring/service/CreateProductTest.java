package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Body;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;

// sa testam cu app pornita
// - @SpringBootTest
// - porniti o DB in memorie
// - @Mock->@MockBean

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
@AutoConfigureWireMock(port = 9999)
public class CreateProductTest {
  @Autowired // creeaza un Mock cu mockito pe care il pune ca si bean in Spring
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductService productService;

//  @BeforeEach
//  @AfterEach
//  public void cleanupDB() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();
//  }
  @Autowired
  WireMockServer wireMock;
  @Test
  void createThrowsForUnsafeProduct() {
    wireMock.stubFor(get(urlMatching("/product/upc-unsafe/safety"))
        .willReturn(jsonResponse("""
            {
              "category": "DETERMINED",
              "detailsUrl": "http://details.url/a/b"
            }
            """, 200)));

    //when(safetyClient.isSafe("upc-unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "upc-unsafe", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  @WithMockUser(username = "user", roles = "ADMIN")
  void createOk() {
    Supplier supplier = supplierRepo.save(new Supplier());
    //when(safetyClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "upc-safe", supplier.getId(), HOME);

    // WHEN
    productService.createProduct(dto);
    System.out.println("inapoi");

    // a)
    List<Product> tate = productRepo.findAll();
    assertThat(tate).hasSize(1);
    Product product = tate.get(0);
    // b) product = repo.findByName("name")
    // c) productId = productService.createProduct(dto);   product = repo.findById(productId);

    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getUpc()).isEqualTo("upc-safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void createWithoutCategory() {
    Supplier supplier = supplierRepo.save(new Supplier());
    //when(safetyClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "upc-safe", supplier.getId(), null);

    // WHEN
    productService.createProduct(dto);

    Product product = productRepo.findById(id).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
