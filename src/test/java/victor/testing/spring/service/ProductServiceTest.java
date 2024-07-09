package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

// they conmfigure the instance of the test class
//@RunWith(MockitoJUnitRunner.class)// Junit 4
@ExtendWith(MockitoExtension.class) // JUnit 5
class ProductServiceTest {
  @Mock // works thanks to MockitoExtension/Runner, created after instantiation
  SupplierRepo supplierRepoMock;// = mock(SupplierRepo.class);
  @Mock
  ProductRepo productRepoMock;
  @Mock
  SafetyApiClient safetyApiClientMock;
  @Mock
  KafkaTemplate<String, String> kafkaTemplateMock;

  // field = new runs at constructor time (too early, before @Mock fields got created)
  ProductService service = new ProductService(
      supplierRepoMock,
      productRepoMock,
      safetyApiClientMock,
      null,
      kafkaTemplateMock);

  @Test
  void createProduct() {
    // given
    Mockito.when(safetyApiClientMock.isSafe(ArgumentMatchers.any())).thenReturn(true);
    //unstubbed method return default 'absent' values when called: false, 0, null, Optional.empty(), List.of()
    Mockito.when(supplierRepoMock.findByCode(ArgumentMatchers.any())).thenReturn(Optional.of(new Supplier()));

    // when
    ProductDto dto = new ProductDto();
    dto.setName("name");// WE NEVER STUB GETTERS. METHODS OF DATA OBJECTS.
    // we only Mockito.mock() classes with behavior, not carrying state.

    service.createProduct(dto);

    // then
  }
}