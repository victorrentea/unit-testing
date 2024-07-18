package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.design.time.TimeExtension;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

//@MockitoSettings(strictness = Strictness.LENIENT) // DO NOT USE THIS!±!
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
  public static final String BARCODE = "BARCODE";
  public static final String SUPPLIER_CODE = "SUPPLIER_CODE";
  public static final long NEW_PRODUCT_ID = 42L;
  public static final LocalDateTime CHRISTMAS = LocalDateTime.parse("2020-12-25T00:00:00");
  public static final String PRODUCT_NAME = "NAME";
  @Mock
  SupplierRepo supplierRepo;
  @RegisterExtension
  TimeExtension timeExtension = new TimeExtension(CHRISTMAS);
  //  @Mock
//  TimeFactory timeFactory;
  @Mock
  ProductRepo productRepo;
  @Mock// always use @
  SafetyApiAdapter safetyApiAdapter;// = mock(SafetyApiAdapter.class);
  @Captor
  ArgumentCaptor<Product> productCaptor;
  @Mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  //  @InjectMocks
  ProductService productService;
  private ProductDto dto = new ProductDto()
      .setName(PRODUCT_NAME)
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE);

  @BeforeEach
  final void thisIsASocialUnitTest() { // test 2 classes together
    productService = new ProductService(
        supplierRepo,
        productRepo,
        safetyApiAdapter,
        new ProductMapper(),
        new TimeFactory(),
        kafkaTemplate);
  }

  @BeforeEach
  final void setup() {
    when(safetyApiAdapter.isSafe(BARCODE)).thenReturn(true);
  }

  @Test
  void failsForUnsafeProduct() {
    when(safetyApiAdapter.isSafe(BARCODE)).thenReturn(false); // override the 'standard' beh in @BeforeEach

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void sendsKafkaEvent() {
    when(supplierRepo.findByCode(SUPPLIER_CODE)).thenReturn(Optional.of(new Supplier()));
    when(productRepo.save(any())).thenReturn(new Product().setId(NEW_PRODUCT_ID));

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

  @Test
  void savesProduct() {
    Supplier supplier = new Supplier();
    when(supplierRepo.findByCode(SUPPLIER_CODE)).thenReturn(Optional.of(supplier));
    when(productRepo.save(any())).thenReturn(new Product().setId(NEW_PRODUCT_ID));

    Long productId = productService.createProduct(dto);

    assertThat(productId).isEqualTo(NEW_PRODUCT_ID);
    verify(productRepo).save(productCaptor.capture());
    Product product = productCaptor.getValue();
    assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
    assertThat(product.getBarcode()).isEqualTo(BARCODE);
    assertThat(product.getSupplier()).isEqualTo(supplier);
  }
}
