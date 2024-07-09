package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

// they conmfigure the instance of the test class
//@RunWith(MockitoJUnitRunner.class)// Junit 4
@ExtendWith(MockitoExtension.class) // JUnit 5
class ProductServiceTest {
  public static final String BARCODE = "barcode";
  @Mock
  //any stubbing when().then.. you do on mocks created with @Mock
  // HAVE TO BE USED by tested code. If they aren't, the test fails by default
      // since mockito 2.0
  ProductRepo productRepoMock;
  @Mock
  SafetyApiClient safetyApiClientMock;
  @Mock // works thanks to MockitoExtension/Runner, created after instantiation
  SupplierRepo supplierRepoMock;// = mock(SupplierRepo.class);
  @Mock
  KafkaTemplate<String, String> kafkaTemplateMock;
  @InjectMocks // if you only inject mocks (no real object)
  ProductService service;
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
  void createProduct() {
    when(safetyApiClientMock.isSafe(BARCODE)).thenReturn(true);
    when(supplierRepoMock.findByCode(any())).thenReturn(Optional.of(new Supplier()));

    ProductDto dto = new ProductDto();
    dto.setBarcode(BARCODE);
    dto.setName("name");// WE NEVER STUB GETTERS. METHODS OF DATA OBJECTS.
    // we only Mockito.mock() classes with behavior, not carrying state.

    // when
    service.createProduct(dto);


  }
}