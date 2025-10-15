package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

@Transactional
//@TestPropertySource(properties = "prop=altaValoare",locations = "un-prop.properties") // RAU
//@ActiveProfiles("siAltu") // RAU: diferenta care porneste inca 1 spring = +20s
public class ProductServiceCreate2Test extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
//  @MockitoBean // inlocuieste beanul real cu un Mock- ratam obiectivul sa ne apropiem de realitate -@simona
//  SafetyApiAdapter safetyApiAdapter;
  @MockitoBean //RAU daca scoti
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();

  @Test
  void createThrowsForUnsafeProduct() {
    productDto = productDto.withBarcode("barcode-uxynsafe");
//    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);
    WireMock.stubFor(
        get(
                urlEqualTo("/product/barcode-uxynsafe/safety"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                    {
                      "detailsUrl": "http://details.url/a/b",
                      "category": "UNSAFE"
                    }
                """))
    );


    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  @WithMockUser(username = "pink")
  void createOk() throws ExecutionException, InterruptedException {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
//    verify(kafkaTemplate).send(
//        eq(ProductService.PRODUCT_CREATED_TOPIC),
//        eq("k"),
//        assertArg(e-> assertThat(e.productId()).isEqualTo(newProductId)));
    assertThat(product.getCreatedDate()).isToday(); // TODO can only integration-test as it requires Hibernate magic
    assertThat(product.getCreatedBy()).isEqualTo("pink");
  }
  @Test
  void createProductWithoutCategory() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe").withCategory(null);
//    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    // WHEN
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