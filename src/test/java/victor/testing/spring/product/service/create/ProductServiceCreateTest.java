package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;

@SpringBootTest // porneste spring context
@ActiveProfiles("db-mem")
public class ProductServiceCreateTest {
  @MockBean // inlocuieste beanul real SafetyClient cu un Mockito.mock() pe care ti-l pune si aici sa-l configurezi, auto-reset intre @Teste
  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @MockBean
  ProductRepo productRepo;
  @MockBean
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

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
    Supplier supplier = new Supplier().setId(13L);
    when(supplierRepo.findById(supplier.getId()))
        .thenReturn(Optional.of(supplier));
    ProductDto dto = new ProductDto("name", "safebar", supplier.getId(), HOME);

    productService.createProduct(dto);

    ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
    verify(productRepo).save(productCaptor.capture());
    Product product = productCaptor.getValue();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

}
