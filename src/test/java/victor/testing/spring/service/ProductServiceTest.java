package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.rest.dto.ProductDto;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // asta interpreteaza @ din clasa
class ProductServiceTest {
  public static final String BARCODE = "#barcode#";
  @Mock
  SafetyApiAdapter apiAdapter;
  @InjectMocks
  ProductService target;

  @Test
  void createProduct() {
    ProductDto dto = new ProductDto();
    dto.setBarcode(BARCODE);
    when(apiAdapter.isSafe(BARCODE)).thenReturn(false);
//    when(apiAdapter.isSafe(any())).thenReturn(false);

    assertThrows(IllegalStateException.class, () ->
        target.createProduct(dto));
  }
}