package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
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

@SpringBootTest
@ActiveProfiles("test")
class CreateProductIntegrationTest {
  public static final String BARCODE = "barcode";
  public static final String SUPPLIER_CODE = "S";
  public static final String PRODUCT_NAME = "name";
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @MockBean
  SafetyApiClient safetyApiClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductService service;

  ProductDto dto = new ProductDto()
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE)
      .setName(PRODUCT_NAME)
      .setCategory(HOME);

  @BeforeEach // #1 manual delete using the repo
  final void setup() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();// now correct
  }
  @Test
  void failsForUnsafeProduct() {
    when(safetyApiClient.isSafe(BARCODE)).thenReturn(false);

    assertThatThrownBy(() -> service.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void savesTheProduct() {
    supplierRepo.save(new Supplier().setCode(SUPPLIER_CODE)); // INSERT of data which is SELECTED by the testeed code
    when(safetyApiClient.isSafe(BARCODE)).thenReturn(true);

    // prod call
    Long productId = service.createProduct(dto);

    Product product = productRepo.findById(productId).get();
    assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
    assertThat(product.getBarcode()).isEqualTo(BARCODE);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getSupplier().getCode()).isEqualTo(SUPPLIER_CODE);
  }

  @Test
  void sendsKafkaMessage() {
    supplierRepo.save(new Supplier().setCode(SUPPLIER_CODE)); // INSERT of data which is SELECTED by the testeed code
    when(safetyApiClient.isSafe(BARCODE)).thenReturn(true);

    service.createProduct(dto);

    verify(kafkaTemplate).send(PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void defaultsCategoryToUncategorized() {
    supplierRepo.save(new Supplier().setCode(SUPPLIER_CODE)); // INSERT of data which is SELECTED by the testeed code
    when(safetyApiClient.isSafe(BARCODE)).thenReturn(true);
    dto.setCategory(null);

    long productId = service.createProduct(dto);

    Product product = productRepo.findById(productId).get();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}