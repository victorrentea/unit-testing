package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductService;
import victor.testing.spring.product.api.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;
// am uitat sa rulez TOATE TESTELE DECENTE INAINTE DE COMIT
//@Tag("slow")
@SpringBootTest // porneste spring context
@ActiveProfiles("db-mem")
public class ProductServiceCreate3Test {
  @MockBean // inlocuieste beanul real SafetyClient cu un Mockito.mock() pe care ti-l pune si aici sa-l configurezi, auto-reset intre @Teste
  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

  @AfterEach// nu e sufient
  @BeforeEach // asa da
  final void before() {
    // in ordinea FK domle!
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }

  @Disabled
  @Test
  void throwsForUnsafeProduct() {
    when(safetyClient.isSafe("bar")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: bar");
  }

  @Test
  void ok() {
    when(safetyClient.isSafe("safebar")).thenReturn(true);
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    productService.createProduct(dto);

    // FIND BY UNIQUE CRITERIA⭐️
//    Product product = productRepo.findByName("name"); // find in DB dupa 1 criteriu
//    Product product = productRepo.findById("name"); // find in DB dupa 1 criteriu

    // PRESUPUNAND CA BAZA INITIAL A FOST GOALA
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
     assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void missingCategoryDefaultsToUNCATEGORIZED() {
    when(safetyClient.isSafe("safebar")).thenReturn(true);
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    ProductDto dto = new ProductDto("name",
        "safebar", supplierId, null);

    /*productId=*/productService.createProduct(dto);

//    assertThat(productRepo.findAll()).hasSize(1);
//    Product product = productRepo.findAll().get(0);
    Product product = productRepo.findByName("name");
    // cel mai sfant era dupa ID-ul nou atribuit din prod asa:
//    Product product = productRepo.findById(productId).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
