package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public class CreateProduct2Test extends IntegrationTest {
  @Autowired
  ProductService productService;

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyClient.isSafe("upc-unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "upc-unsafe", "S", HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() throws InterruptedException {
    when(supplierRepo.findByCode("S")).thenReturn(Optional.of(new Supplier().setCode("S")));
    when(safetyClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "upc-safe", "S", HOME);

    // WHEN
    productService.createProduct(dto);
  Thread.sleep(5000);
    ArgumentCaptor<Product> productCaptor = forClass(Product.class);
    verify(productRepo).save(productCaptor.capture()); // as the mock the actual param value
    Product product = productCaptor.getValue();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getUpc()).isEqualTo("upc-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    //assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    //assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

}
