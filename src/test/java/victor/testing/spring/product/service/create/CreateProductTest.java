package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductService;
import victor.testing.spring.product.api.dto.ProductDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;

//@ExtendWith(MockitoExtension.class)
@ActiveProfiles("db-mem")
@SpringBootTest
public class CreateProductTest {
  @MockBean
  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyClient.isSafe("bar")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: bar");
  }

  @Test
  void createOk() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(safetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }
  @Test
  void createOkCuCategoryNull() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(safetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, null);

    // WHEN
    productService.createProduct(dto);

    // THEN
    assertThat(productRepo.findAll()).hasSize(1);
    Product product = productRepo.findAll().get(0);
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
