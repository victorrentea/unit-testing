package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // asta interpreteaza @ din clasa
class ProductServiceTest {
  public static final String BARCODE = "#barcode#";
  @Mock
  SafetyApiAdapter apiAdapter;
  @Mock
  SupplierRepo supplierRepo;
  @Mock
  ProductRepo productRepo;
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
//    Supplier supplier = new Supplier(); // BINE
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
  }
}