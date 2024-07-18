package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

//@MockitoSettings(strictness = Strictness.LENIENT) // DO NOT USE THIS!±!
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
  public static final String BARCODE = "BARCODE";
  public static final String SUPPLIER_CODE = "SUPPLIER_CODE";
  public static final long NEW_PRODUCT_ID = 42L;
  public static final LocalDateTime CHRISTMAS = LocalDateTime.parse("2020-12-25T00:00:00");
  @Mock
  SupplierRepo supplierRepo;
  @Mock
  TimeFactory timeFactory;
  @Mock
  ProductRepo productRepo;
  @Mock// always use @
  SafetyApiAdapter safetyApiAdapter;// = mock(SafetyApiAdapter.class);
  @Mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
//  @InjectMocks
  ProductService productService;
  private ProductDto dto = new ProductDto()
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE);

  @BeforeEach
  final void thisIsASocialUnitTest() { // test 2 classes together
    productService = new ProductService(
        supplierRepo,
        productRepo,
        safetyApiAdapter,
        new ProductMapper(),
        timeFactory,
        kafkaTemplate);
  }

  @Test
  void failsForUnsafeProduct() {
    when(safetyApiAdapter.isSafe(BARCODE)).thenReturn(false);

    assertThatThrownBy(()->productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void sendsKafkaEvent() {
    when(safetyApiAdapter.isSafe(BARCODE)).thenReturn(true);
    when(supplierRepo.findByCode(SUPPLIER_CODE))
        .thenReturn(Optional.of(new Supplier()));
    when(productRepo.save(any())) //TODO reflect later. should I do something else ?
        .thenReturn(new Product().setId(NEW_PRODUCT_ID));
    when(timeFactory.now()).thenReturn(CHRISTMAS);

    Long id = productService.createProduct(dto);

    ProductCreatedEvent expectedEvent = new ProductCreatedEvent(id, CHRISTMAS);
    verify(kafkaTemplate).send(PRODUCT_CREATED_TOPIC, "k", expectedEvent);
//    verify(kafkaTemplate).send(
//        eq(PRODUCT_CREATED_TOPIC),
//        eq("k"),
//        argThat(event -> event.productId() == NEW_PRODUCT_ID &&
//            event.observedAt().equals(CHRISTMAS)
             // the ovservedAt is close by max 40 ms to now
//             event.observedAt().isAfter(LocalDateTime.now().minus(40, ChronoUnit.MILLIS)) &&
//             event.observedAt().isBefore(LocalDateTime.now().plus(40, ChronoUnit.MILLIS))
//             )); // time is different

//            event.observedAt().isBefore(LocalDateTime.now().plusSeconds(1)))); // time is different
//        eq(new ProductCreatedEvent(id, LocalDateTime.now()))); // time is different
  }
}
