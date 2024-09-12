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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // asta interpreteaza @ din clasa
class ProductServiceTest {
  @Mock
  SafetyApiAdapter apiAdapter;// = Mockito.mock(SafetyApiAdapter.class);
  @InjectMocks
  ProductService target;/* = new ProductService(
      null,
      null,
      apiAdapter,
      null,
      null);*/

  @Test
  void createProduct() {
    when(apiAdapter.isSafe("123"))
        .thenReturn(false);

    assertThrows(IllegalStateException.class, () ->
        target.createProduct(new ProductDto()));
  }
}