package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;
import org.springframework.web.client.RestTemplate;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import victor.testing.spring.SpringApplication;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.entity.ProductCategory.HOME;

@SpringBootTest// + DB : a) in-mem or b) test-containered
@EmbeddedKafka//a) or b) test-containered

@EnableWireMock
@ActiveProfiles("test")
//@SpringBootTest(classes = {
//    ProductService.class,
//    SafetyApiAdapter.class,
//    RestTemplate.class
//})// + don't boot the DB interaction at
@MockitoBeans(@MockitoBean(types = {ProductMapper.class}))
public class ProductServiceCreateTest {
  @Autowired // replaces the normal bean (injectable) class in Spring with a mock you can configure.
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockitoBean
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
    stubFor(get(urlEqualTo("/product/barcode-unsafe/safety"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
            {
              "detailsUrl": "http://details.url/a/b",
              "category": "UNSAFE"
            }
        """)));
    productDto = productDto.withBarcode("barcode-unsafe");

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    stubFor(get(urlEqualTo("/product/barcode-safe/safety"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
            {
              "detailsUrl": "http://details.url/a/b",
              "category": "SAFE"
            }
        """)));
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        assertArg(e-> assertThat(e.productId()).isEqualTo(newProductId)));
//    assertThat(product.getCreatedDate()).isToday(); // TODO can only integration-test as it requires Hibernate magic
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