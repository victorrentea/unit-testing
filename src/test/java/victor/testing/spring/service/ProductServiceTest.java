package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
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

class ProductServiceTest {
  SupplierRepo supplierRepoMock = mock(SupplierRepo.class);
  ProductRepo productRepoMock = mock(ProductRepo.class);
  SafetyApiClient safetyApiClientMock = mock(SafetyApiClient.class);
  KafkaTemplate<String, String> kafkaTemplateMock = mock(KafkaTemplate.class);
  ProductService service = new ProductService(supplierRepoMock, productRepoMock, safetyApiClientMock,
      null, kafkaTemplateMock);

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