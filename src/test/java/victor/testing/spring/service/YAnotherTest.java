package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.BaseIntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

class YAnotherTest extends BaseIntegrationTest {
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
  @MockBean// kills performance because spring context is differently configured than
      // the one created for the other test class
      // HERE: product Mapper is a mock
      // THERE: product Mapper is a real object
  ProductMapper productMapper;// bad idea

  ProductDto dto = new ProductDto()
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE)
      .setName(PRODUCT_NAME)
      .setCategory(HOME);

  @Test
  void savesTheProduct() {
    when(safetyApiClient.isSafe(BARCODE)).thenReturn(true);

    // prod call
    Long productId = service.createProduct(dto);

    Product product = productRepo.findById(productId).get();
    assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
    assertThat(product.getBarcode()).isEqualTo(BARCODE);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getSupplier().getCode()).isEqualTo(SUPPLIER_CODE);
  }

}