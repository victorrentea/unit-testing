package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.rest.dto.ProductDto;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ProductServiceTest {
  SafetyApiAdapter apiAdapter = Mockito.mock(SafetyApiAdapter.class);
  ProductService target = new ProductService(
      null,
      null,
      apiAdapter,
      null,
      null);

  @Test
  void createProduct() {
    when(apiAdapter.isSafe("123"))
        .thenReturn(false);

    assertThrows(IllegalStateException.class, () ->
        target.createProduct(new ProductDto()));
  }
}