package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.api.dto.ProductDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

@ExtendWith(MockitoExtension.class)
public class CreateProductTest {
  @Mock
  SupplierRepo supplierRepo;
  @Mock
  ProductRepo productRepo;
  @Mock
  SafetyClient safetyClient;
  @Mock
  KafkaTemplate<String, String> kafkaTemplate;
  @InjectMocks
  ProductService productService;

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyClient.isSafe("upc-unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "upc-unsafe", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    Supplier supplier = new Supplier().setId(13L);
    when(supplierRepo.findById(supplier.getId())).thenReturn(Optional.of(supplier));
    when(safetyClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "upc-safe", supplier.getId(), HOME);

    // WHEN
    productService.createProduct(dto);

    ArgumentCaptor<Product> productCaptor = forClass(Product.class);
    verify(productRepo).save(productCaptor.capture()); // as the mock the actual param value
    Product product = productCaptor.getValue();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getUpc()).isEqualTo("upc-safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
    assertThat(product.getCategory()).isEqualTo(HOME);
    //assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    //assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

}
