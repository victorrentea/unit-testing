package victor.testing.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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
import static java.time.LocalDateTime.parse;
import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@ExtendWith(MockitoExtension.class) // asta interpreteaza @ din clasa
class ProductServiceTest {
  public static final String BARCODE = "#barcode#";
  @Mock
  SafetyApiAdapter apiAdapter;
  @Mock
  SupplierRepo supplierRepo;
  @Mock
  ProductRepo productRepo;
  @Mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @InjectMocks
  ProductService target;

  @Test
//  void whenProductIsNotSafe_createShouldThrowIllegalStateException() {
//  void create_whenProductIsNotSafe_shouldThrowIllegalStateException() {
  void createThrows_forUnsafeProduct() {
    ProductDto dto = new ProductDto();
    dto.setBarcode(BARCODE);
    when(apiAdapter.isSafe(BARCODE)).thenReturn(false);

    assertThrows(IllegalStateException.class, () ->
        target.createProduct(dto));
  }

  @Test
  void createProduct() {
    Supplier supplier = mock(Supplier.class); // RAU
    ProductDto dto = new ProductDto()
        .setBarcode(BARCODE)
        .setSupplierCode("#supplierCode#");
    when(apiAdapter.isSafe(BARCODE)).thenReturn(true);
    when(supplierRepo.findByCode("#supplierCode#"))
        .thenReturn(Optional.of(supplier));
    when(productRepo.save(any()))
        .thenReturn(new Product().setId(13L));

    target.createProduct(dto);

    ArgumentCaptor<ProductCreatedEvent> captor = forClass(ProductCreatedEvent.class);
    verify(kafkaTemplate).send(
        eq(PRODUCT_CREATED_TOPIC),
        eq("k"),
//        any()
        captor.capture()
    );
    ProductCreatedEvent event = captor.getValue();
    assertEquals(13L, event.productId());
//    assertEquals(now(), event.observedAt());

    assertThat(event.observedAt())
        .isCloseTo(now(), byLessThan(1, SECONDS));
  }
  @Test
  void createProduct_mockuindTimpul() {
    Supplier supplier = mock(Supplier.class); // RAU
    ProductDto dto = new ProductDto()
        .setBarcode(BARCODE)
        .setSupplierCode("#supplierCode#");
    when(apiAdapter.isSafe(BARCODE)).thenReturn(true);
    when(supplierRepo.findByCode("#supplierCode#"))
        .thenReturn(Optional.of(supplier));
    when(productRepo.save(any()))
        .thenReturn(new Product().setId(13L));

    LocalDateTime christmas = parse("2021-12-25T00:00:00");

    try (var staticMock = mockStatic(LocalDateTime.class)) {
      staticMock.when(LocalDateTime::now).thenReturn(christmas);
      target.createProduct(dto);
    }

    // nu merita captor daca vrei sa verific 1 singur camp
//    var captor = forClass(ProductCreatedEvent.class);
//    verify(kafkaTemplate).send(eq(PRODUCT_CREATED_TOPIC), eq("k"), captor.capture());
//    ProductCreatedEvent event = captor.getValue();
//    assertThat(event.observedAt()).isEqualTo(christmas);

    // ci asa
    verify(kafkaTemplate).send(eq(PRODUCT_CREATED_TOPIC), eq("k"),
        argThat(event -> event.observedAt().equals(christmas)));
  }
}