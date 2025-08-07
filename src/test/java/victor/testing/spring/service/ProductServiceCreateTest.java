package victor.testing.spring.service;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

@ExtendWith(MockitoExtension.class)
class ProductServiceCreateTest {
  @Mock
  SupplierRepo supplierRepo;
  @Mock
  ProductRepo productRepo;
  @Mock
  SafetyApiAdapter safetyApiAdapter;
  @Mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @InjectMocks
  ProductService productService;

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();

  @Test
  void createThrowsForUnsafeProduct() {
    productDto = productDto.withBarcode("barcode-unsafe");
    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    when(supplierRepo.findByCode("S")).thenReturn(Optional.of(new Supplier().setCode("S")));
    productDto = productDto.withBarcode("barcode-safe");
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
    when(productRepo.save(any())).thenReturn(new Product().setId(123L));

    // WHEN
    Long productId = productService.createProduct(productDto);

    ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
    verify(productRepo).save(productCaptor.capture()); // dear mock give me the param you remember from when prod called you
    Product product = productCaptor.getValue();
    assertThat(product)
      .returns("name",Product::getName)
      .returns("barcode-safe", Product::getBarcode)
      .returns("S", p -> p.getSupplier().getCode())
      .returns(HOME, Product::getCategory);

    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC), // Pro: syntax reference
        eq("k"),
        productCreatedEventCaptor.capture()
    );
    ProductCreatedEvent event = productCreatedEventCaptor.getValue();
    assertThat(event.productId()).isEqualTo(productId);
    assertThat(event.observedAt()).isCloseTo(now(), byLessThan(1, MINUTES));
  }
  @Captor
  ArgumentCaptor<ProductCreatedEvent> productCreatedEventCaptor;
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