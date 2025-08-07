package victor.testing.spring.service;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.tools.CaptureSystemOutput;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka // a kind of H2
class ProductServiceCreateTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockitoBean // replaces the real bean with a mockito mock
  SafetyApiAdapter safetyApiAdapter;
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
  @CaptureSystemOutput
  void createThrowsForUnsafeProduct(CaptureSystemOutput.OutputCapture outputCapture) {
    productDto = productDto.withBarcode("barcode-unsafe");
    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
    assertThat(outputCapture.toString()).contains("[ALARM-CALL-LEGAL]");

  }

  @Test
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    productDto = productDto.withBarcode("barcode-safe");
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);

    // WHEN
    Long productId = productService.createProduct(productDto);

    Product product = productRepo.findById(productId).orElseThrow();
    assertThat(product)
      .returns("name",Product::getName)
      .returns("barcode-safe", Product::getBarcode)
      .returns("S", p -> p.getSupplier().getCode())
      .returns(HOME, Product::getCategory);
    assertThat(product.getCreatedDate()).isToday(); // spring+jpa magic
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC), // Pro: syntax reference
        eq("k"),
//        argThat(event -> event.productId().equals(productId)) // if only one attr is interesting
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