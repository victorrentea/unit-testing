package victor.testing.spring.service;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

// Acelasi scenariu ca in ProductServiceCreateTest, dar cu contextul Spring redus la minimum.
//
// De ce nu @SpringBootTest: acela porneste TOT ce vede @ComponentScan de sub
// @SpringBootApplication + toate auto-configurarile: DataSource + Flyway + Hibernate,
// Spring Security, @Scheduled, web layer... plus @EmbeddedKafka (un broker Kafka in-memory).
// Pentru a testa 15 linii de logica din createProduct nu am nevoie de nimic din toate astea.
//
// Cum am construit contextul asta, progresiv de la nimic:
//  PAS 1: @SpringJUnitConfig(ProductService.class) = context Spring gol, in care singura
//         definitie de bean e chiar clasa testata. Spring o instantiaza prin constructorul
//         generat de @RequiredArgsConstructor si incearca sa-i injecteze dependintele.
//         => a picat cu NoSuchBeanDefinitionException: 'SupplierRepo'.
//  PAS 2: adaug cate un @MockitoBean pentru fiecare dependinta ceruta de constructor,
//         pana cand contextul porneste. @MockitoBean = pune un Mockito.mock in contextul
//         Spring SI mi-l da si mie in camp, ca sa-i pot dicta comportamentul din test.
//         Nici o alta clasa din aplicatie nu e incarcata.
//
// Rezultat: din aplicatie se incarca doar ProductService + cele 5 mock-uri, nimic altceva.
// Nu se atinge baza de date, Kafka sau reteaua.
// Masurat pe masina mea: 1.1s clasa asta, fata de 7.6s ProductServiceCreateTest (@SpringBootTest).
@SpringJUnitConfig(ProductService.class)
public class ProductServiceCreateSliceTest {
  @MockitoBean
  SupplierRepo supplierRepo;
  @MockitoBean
  ProductRepo productRepo;
  @MockitoBean
  SafetyApiClient safetyApiClient;
  @MockitoBean // nefolosit de createProduct, dar cerut de constructorul lui ProductService
  ProductMapper productMapper;
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
    productDto = productDto.withBarcode("barcode-unsafe");
    when(safetyApiClient.isSafe("barcode-unsafe")).thenReturn(false);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    when(supplierRepo.findByCode("S")).thenReturn(Optional.of(new Supplier().setCode("S")));
    productDto = productDto.withBarcode("barcode-safe");
    when(safetyApiClient.isSafe("barcode-safe")).thenReturn(true);
    when(productRepo.save(any())).thenReturn(new Product().setId(123L));

    // WHEN
    var newProductId = productService.createProduct(productDto);

    ArgumentCaptor<Product> productCaptor = forClass(Product.class);
    verify(productRepo).save(productCaptor.capture());
    Product product = productCaptor.getValue();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("key"),
        assertArg(event -> assertThat(event.productId()).isEqualTo(newProductId)));
  }
}
