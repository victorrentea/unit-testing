package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.inject.Inject;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.wiremock.spring.EnableWireMock;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.entity.ProductCategory.HOME;

//    var dto = new DtoBun()
//        .setX(1)
//        .setY(2);//❤️❤️❤️ vezi lombok.config
@SpringBootTest
@ActiveProfiles("test") // application-test.properties in care suprascriu prop
@EmbeddedKafka // in mem
@EnableWireMock // citeste automat din /src/test/resources/mappings/*.json
// DB in teste poate fi H2(in-mem), ORA in testcontainer, pe o baza dedicata de test pt ca schema ta are 667 de tabele cu 2.4g date goala.
public class ProductServiceCreateTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
//  @MockitoBean // inlocuieste in app spring pornita beanul real cu un Mockito.mock pe care-l injecteaza si aici sa poti sa-l inveti ce vrei sa faca
//  SafetyApiClient safetyApiClient;
  @MockitoBean
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Inject
  ProductService productService;

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();

  @Test
  void createThrowsForUnsafeProduct() {
    stubFor(get(urlEqualTo("/product/barcode-unsafe-dsl/safety"))
        .willReturn(okJson("""
            {
              "detailsUrl": "http://details.url/a/b",
              "category": "%s"
            }
            """.formatted("UNSAFE"))));

    // barcode diferit de cel din .json, ca sa fiu sigur ca se foloseste stubul meu
    productDto = productDto.withBarcode("barcode-unsafe-dsl");

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");

    // Verific ca apelul HTTP asteptat chiar a plecat spre Safety API.
    // Prefix-ul 'WireMock.' e obligatoriu: 'verify' e importat static si din Mockito, si din WireMock.
    WireMock.verify(getRequestedFor(urlEqualTo("/product/barcode-unsafe-dsl/safety")));
  }

  @Test
  @WithMockUser(username = "john")
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");
//    when(safetyApiClient.isSafe("barcode-safe"))
//        .thenReturn(true);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("key"),
        assertArg(event -> assertThat(event.productId()).isEqualTo(newProductId)));
    assertThat(product.getCreatedBy()).isEqualTo("john"); // TODO test framework magic
    // Product.createdDate e LocalDate (fara ora), deci nu am ce compara la nivel de secunde:
    // within(4, SECONDS) arunca UnsupportedTemporalTypeException. Ca sa pot verifica
    // "creat acum max 4 secunde" ar trebui ca entitatea sa tina LocalDateTime/Instant.
    assertThat(product.getCreatedDate()).isToday();
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