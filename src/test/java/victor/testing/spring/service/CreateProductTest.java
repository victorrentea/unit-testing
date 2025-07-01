package victor.testing.spring.service;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

// TODO COVERT to @SpringBootTest

@ExtendWith(MockitoExtension.class)
public class CreateProductTest {
  @Mock
  SupplierRepo supplierRepo;
  @Mock
  ProductRepo productRepo;
  @Mock
  SafetyApiAdapter safetyApiAdapter;
  @Mock
  SqsTemplate sqsTemplate;
  @InjectMocks
  ProductService productService;

  ProductDto productDto = ProductDto.builder()
      .name("name")
      .supplierCode("S")
      .category(HOME)
      .build();
  @BeforeEach
  final void before() {
      productService.productCreatedTopicName = "topic";
  }

  @Test
  void createThrowsForUnsafeProduct() {
    productDto = productDto.withBarcode("barcode-unsafe");
    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    when(supplierRepo.findByCode("S")).thenReturn(Optional.of(new Supplier().setCode("S")));
    productDto = productDto.withBarcode("barcode-safe");
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
    when(productRepo.save(any())).thenReturn(new Product().setId(123L));

    // WHEN
    var newProductId = productService.createProduct(productDto);

    ArgumentCaptor<Product> productCaptor = forClass(Product.class);
    verify(productRepo).save(productCaptor.capture()); // as the mock the actual param value
    Product product = productCaptor.getValue();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(sqsTemplate).send(
        eq(productService.productCreatedTopicName),
        assertArg((ProductCreatedEvent e)-> assertThat(e.productId()).isEqualTo(newProductId)));
  }

}