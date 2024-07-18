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
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@MockitoSettings(strictness = Strictness.LENIENT) // DO NOT USE THIS!±!
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
  public static final String BARCODE = "BARCODE";
  @Mock
  SupplierRepo supplierRepo;
  @Mock
  ProductRepo productRepo;
  @Mock// always use @
  SafetyApiAdapter safetyApiAdapter;// = mock(SafetyApiAdapter.class);
  @Mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
//  @InjectMocks
  ProductService productService;

  @BeforeEach
  final void thisIsASocialUnitTest() { // test 2 classes together
    productService = new ProductService(
        supplierRepo,
        productRepo,
        safetyApiAdapter,
        new ProductMapper(),
        kafkaTemplate);
  }

  @Test
  void failsForUnsafeProduct() {
    when(safetyApiAdapter.isSafe(BARCODE)).thenReturn(false);
    ProductDto dto = new ProductDto().setBarcode(BARCODE);

    assertThatThrownBy(()->productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }
}
