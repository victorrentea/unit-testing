package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;
import static reactor.core.publisher.Mono.when;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.service.ProductServiceCreateTest.BARCODE;
import static victor.testing.spring.service.ProductServiceCreateTest.SUPPLIER_CODE;

// we can mock local method calls
@ExtendWith(MockitoExtension.class)
public class DontDoIt_TheHackerWay {
  @Mock
  SafetyApiClient safetyApiClient;
  @Mock
  KafkaTemplate kafkaTemplate;
  @Mock
  ProductRepo productRepo;
  @Mock
  SupplierRepo supplierRepo;
  @InjectMocks
  @Spy// tells mockito to prepare to mock methods in the same class under test.
  ProductService productService;
  // without mocking frameworks, this would be achiebable
//  ProductService productService = new ProductService(
//      supplierRepo, productRepo, safetyApiClient, new ProductMapper(),
//      kafkaTemplate) {
//    @Override
//    Product newProduct(ProductDto productDto) {
//      return new Product().setName("x"); // fake result
//    }
//  };

  ProductDto dto = new ProductDto()
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE)
      .setName("name")
      .setCategory(HOME);

  @Test
  void experiment() {
    Mockito.when(safetyApiClient.isSafe(BARCODE)).thenReturn(true);
    Mockito.when(supplierRepo.findByCode(SUPPLIER_CODE)).thenReturn(Optional.of(new Supplier()));

    // The hacker way:
    // Goal: I want to when-thenReturn the newProduct method
    // PROBLEM: that method is inside the same class under test
    // a) reflection (bad) - "newProduct" using powermock
//        PowerMockito.when(myClass, "privateMethod").thenReturn("Mocked Private Method");
    // b) subcutaneous testing (good) using @VisibleForTesting
    // IF YOU CAN EDIT THE CODE UNDER TEST..
    //Note: differnt syntax of Mockito + @Spy = partial mock
    doReturn(new Product().setName("x"))
        .when(productService).newProduct(dto);

    productService.createProduct(dto);
  }
}
