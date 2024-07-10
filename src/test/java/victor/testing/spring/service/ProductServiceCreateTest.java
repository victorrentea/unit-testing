package victor.testing.spring.service;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

// they conmfigure the instance of the test class
//@RunWith(MockitoJUnitRunner.class)// Junit 4
@ExtendWith(MockitoExtension.class) // JUnit 5
class ProductServiceCreateTest { // name of the tested method in the test class name
  public static final String BARCODE = "barcode";
  public static final String SUPPLIER_CODE = "S";
  @Mock // generates an implem of that interface
  //any stubbing when().then.. you do on mocks created with @Mock
  // HAVE TO BE USED by tested code. If they aren't, the test fails by default
  // since mockito 2.0
  ProductRepo productRepoMock;
  @Mock // generates a subclass of your class
  SafetyApiClient safetyApiClientMock;
  @Mock // works thanks to MockitoExtension/Runner, created after instantiation
  SupplierRepo supplierRepoMock;// = mock(SupplierRepo.class);
  @Mock
  KafkaTemplate<String, String> kafkaTemplateMock;
  @InjectMocks // if you only inject mocks (no real object)
  ProductService service;

  ProductDto dto = new ProductDto()
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE)
      .setName("name")
      .setCategory(ProductCategory.HOME);
  // call the constructor, or inject  in provate fields all the above @Mocks


  //  @BeforeEach
//  final void setup() { // now works as it runs later
//    // the recommended way to inject both mocks and a real object
//    // = social unit test covering 2+ real classes surrounded by mocks.
//    // as opposed to Solitary Unit Test (1 single alone sad class surrounded by mocks)
//    service = new ProductService(
//        supplierRepoMock,
//        productRepoMock,
//        safetyApiClientMock,
//        new ProductMapper(), // real instance
//        kafkaTemplateMock);
//  }
  @Test
  void failsForUnsafeProduct() {
    when(safetyApiClientMock.isSafe(BARCODE)).thenReturn(false);

    assertThatThrownBy(() -> service.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void ok() {
    when(safetyApiClientMock.isSafe(BARCODE)).thenReturn(true);
    when(supplierRepoMock.findByCode(SUPPLIER_CODE)).thenReturn(of(new Supplier()));

    // when
    service.createProduct(dto);

    // I can ask any mock for the arguments that it was given
    // during the prod call above, this way:
    ArgumentCaptor<Product> productCaptor = forClass(Product.class);
    verify(productRepoMock).save(productCaptor.capture()); // - "Please fill up in this captor the arg"
    Product product = productCaptor.getValue();
    // CHALLENGE: i don't have any reference to the Product that is created in the tested code
    AssertionsForClassTypes.assertThat(product.getName()).isEqualTo("name");
    verify(kafkaTemplateMock).send(PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
//  void productWithoutCategory() {
  void categoryDefaultsToUncategorizedWhenMissing() {
    when(safetyApiClientMock.isSafe(BARCODE)).thenReturn(true);
    when(supplierRepoMock.findByCode(SUPPLIER_CODE)).thenReturn(of(new Supplier()));
    dto.setCategory(null);

    // when
    service.createProduct(dto);

    // too implementation-oriented, not functional
//    assertThat(dto.getCategory()).isEqualTo(ProductCategory.UNCATEGORIZED);

    ArgumentCaptor<Product> captor = forClass(Product.class);
    verify(productRepoMock).save(captor.capture());
    Product product = captor.getValue();
    assertThat(product.getCategory()).isEqualTo(ProductCategory.UNCATEGORIZED);
  }
}