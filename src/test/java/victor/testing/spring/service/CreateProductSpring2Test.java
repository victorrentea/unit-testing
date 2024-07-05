package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyApiClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;


//@Tag("flaky") // ca sa nu rupi increderea echipei in CI
//@Tag("slow") // ca sa termine testele in 15m
public class CreateProductSpring2Test extends IntegrationTest {
  @MockBean
  SafetyApiClient safetyApiClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductService sut;

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyApiClient.isSafe("upc-unsafe")).thenReturn(false);
    ProductDto productDto = new ProductDto("name", "upc-unsafe", "S", HOME);

    assertThatThrownBy(() -> sut.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    when(safetyApiClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto productDto = new ProductDto("name", "upc-safe", "S", HOME);

    Long id = sut.createProduct(productDto);

    Product product = productRepo.findById(id).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getUpc()).isEqualTo("upc-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);

    // FLAKY TEST:
//    Assertions.assertEquals(product.getCreatedDate(), LocalDate.now());
    assertThat(product).returns("name", Product::getName);
//    assertThat(product.getCreatedDate()).isCloseTo(LocalDateTime.now(), byLessThan(1, ChronoUnit.MINUTES));
    assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    //assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void defaultsToUncategorized() {
    when(safetyApiClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto productDto = new ProductDto(
        "name", "upc-safe", "S", null);

    Long id = sut.createProduct(productDto);

    Product product = productRepo.findById(id).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}

/*
wireMockServer.stubFor(get("/product/upc-unsafe/safety")
  .willReturn(okJson("""
      {
       "category": "NOT SAFE",
       "detailsUrl": "http://details.url/a/b"
      }
      """)));
 */