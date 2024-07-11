package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@ExtendWith(MockitoExtension.class)
class CreateProductIntegrationTest {
  public static final String BARCODE = "barcode";
  public static final String SUPPLIER_CODE = "S";
  @Mock
  ProductRepo productRepoMock;
  @Mock
  SafetyApiClient safetyApiClientMock;
  @Mock
  SupplierRepo supplierRepoMock;
  @Mock
  KafkaTemplate<String, String> kafkaTemplateMock;
  @InjectMocks
  ProductService service;
  @Captor
  ArgumentCaptor<Product> productCaptor;

  ProductDto dto = new ProductDto()
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE)
      .setName("name")
      .setCategory(HOME);

  @Test
  void failsForUnsafeProduct() {
    when(safetyApiClientMock.isSafe(BARCODE)).thenReturn(false);

    assertThatThrownBy(() -> service.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void savesTheProduct() {
    when(supplierRepoMock.findByCode(SUPPLIER_CODE)).thenReturn(of(new Supplier()));
    when(safetyApiClientMock.isSafe(BARCODE)).thenReturn(true);

    service.createProduct(dto);

    verify(productRepoMock).save(productCaptor.capture());
    Product product = productCaptor.getValue();

    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo(BARCODE);
    assertThat(product.getCategory()).isEqualTo(HOME);
  }

  @Test
  void sendsKafkaMessage() {
    when(supplierRepoMock.findByCode(SUPPLIER_CODE)).thenReturn(of(new Supplier()));
    when(safetyApiClientMock.isSafe(BARCODE)).thenReturn(true);

    service.createProduct(dto);

    verify(kafkaTemplateMock).send(PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void defaultsCategoryToUncategorized() {
    when(supplierRepoMock.findByCode(SUPPLIER_CODE)).thenReturn(of(new Supplier()));
    when(safetyApiClientMock.isSafe(BARCODE)).thenReturn(true);
    dto.setCategory(null);

    service.createProduct(dto);

    verify(productRepoMock).save(argThat(product ->
        product.getCategory() == UNCATEGORIZED));
  }

}