package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
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

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@Disabled
// they conmfigure the instance of the test class
//@RunWith(MockitoJUnitRunner.class)// Junit 4
@ExtendWith(MockitoExtension.class) // JUnit 5
//@MockitoSettings(strictness = LENIENT) // default is STRICT_STUBS :: all the stubbing in this class becomes lenienet << DON'T ODO IT
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
  @Captor
  ArgumentCaptor<Product> productCaptor;

  ProductDto dto = new ProductDto()
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE)
      .setName("name")
      .setCategory(HOME);
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

  @Nested
  class HappyFlow {
    @BeforeEach
    final void setup() {
      // BAD: if this stubbing is not used, don't fail the test = this stubbing is not important
      /*lenient().*/
      when(supplierRepoMock.findByCode(SUPPLIER_CODE)).thenReturn(of(new Supplier()));
      when(safetyApiClientMock.isSafe(BARCODE)).thenReturn(true);
    }

    @Test
    void savesTheProduct() {
      // when
      service.createProduct(dto);

      // I can ask any mock for the arguments that it was given
      // during the prod call above, this way:
      verify(productRepoMock).save(productCaptor.capture()); // - "Please fill up in this captor the arg"
      Product product = productCaptor.getValue();
      // CHALLENGE: i don't have any reference to the Product that is created in the tested code

//      assertEquals("name", product.getName()); // throws an AssertionError
      assertThat(product.getName()).isEqualTo("name"); // throws an AssertionError
      assertThat(product.getBarcode()).isEqualTo(BARCODE);
      assertThat(product.getCategory()).isEqualTo(HOME);

      // job.status == "COMPLETED"
      // job.errors == empty
      // job.warnings == empty

      // see all failures
//      try (var softly = new AutoCloseableSoftAssertions()) {
//        softly.assertThat(product.getName()).isEqualTo("name");
//        softly.assertThat(product.getBarcode()).isEqualTo(BARCODE);
//        softly.assertThat(product.getCategory()).isEqualTo(HOME);
//      }

//    assertThat(product) // fancier
//        .returns("name", Product::getName)
//        .returns(BARCODE, Product::getBarcode)
//        .returns(HOME, Product::getCategory);
    }

    @Test
    void sendsKafkaMessage() {
      // when
      service.createProduct(dto);

      // then
      verify(kafkaTemplateMock).send(PRODUCT_CREATED_TOPIC, "k", "NAME");
    }

    @Test
    void defaultsCategoryToUncategorized() {
      dto.setCategory(null);

      // when
      service.createProduct(dto);

      // too implementation-oriented, not functional
//    assertThat(dto.getCategory()).isEqualTo(ProductCategory.UNCATEGORIZED);

//    ArgumentCaptor<Product> captor = forClass(Product.class);
//    verify(productRepoMock).save(captor.capture());
//    Product product = captor.getValue();
//    assertThat(product.getCategory()).isEqualTo(ProductCategory.UNCATEGORIZED);

      // simpler form to check a single attribute, the failure message is worse
      verify(productRepoMock).save(argThat(product ->
          product.getCategory() == UNCATEGORIZED));
    }
  }

//  @BeforeEach
//  final void setup() {
//    when(productRepoMock.findById(1L)).thenReturn(Optional.of(product));
//  }
//
//  @Test
//  void service_plus_mapper() {
//    LocalDate date = LocalDate.now();
//    Product product = new Product()
//        .setId(1L)
//        .setName("name")
//        .setBarcode("BARCODE")
//        .setCategory(HOME)
//        .setCreatedDate(date)
//        .setSupplier(new Supplier().setCode("S"));
//
//    ProductDto dto = service.getProduct(1L);
//
//    assertThat(dto.getId()).isEqualTo(1L);
//    assertThat(dto.getName()).isEqualTo("name");
//    assertThat(dto.getBarcode()).isEqualTo("BARCODE");
//    assertThat(dto.getCategory()).isEqualTo(HOME);
//    assertThat(dto.getCreatedDate()).isEqualTo(date);
//    assertThat(dto.getSupplierCode()).isEqualTo("S");
//  }
//  @Test
//  void getProduct2() {
//  }
//  @Test
//  void getProduct3() {
//  }

}