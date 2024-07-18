package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
  public static final String BARCODE = "BARCODE";
  @Mock
  SupplierRepo supplierRepo;
  @Mock
  ProductRepo productRepo;
//  @Mock
  SafetyApiAdapter safetyApiAdapter = mock(SafetyApiAdapter.class);
  @Mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @InjectMocks
  ProductService productService;

  @Test
  void failsForUnsafeProduct() {
    when(safetyApiAdapter.isSafe(BARCODE)).thenReturn(false);
    ProductDto dto = new ProductDto().setBarcode(BARCODE);

    assertThatThrownBy(()->productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }
}
