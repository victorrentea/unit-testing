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

// TODO rulez testul asta cu Spring pornit,
//    luand un bean ProductService de la Spring\
@SpringBootTest // porneste o app Spring in procesul de JUnit
@ActiveProfiles("db-mem")
public class CreateProductTest {
  @MockBean
  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @MockBean
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

  @Test
  void createThrowsForUnsafeProduct() {
    // aici invatam mockul ce sa raspunda:
    // cand te cheama .isSafe cu param "unsafe" sa raspunzi cu "true"
    when(safetyClient.isSafe("unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "unsafe", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: unsafe");
  }

  @Test
  void createOk() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    //when(supplierRepo.findById(supplier.getId())).thenReturn(Optional.of(supplier));
    when(safetyClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safe", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
    ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
    verify(productRepo).save(productCaptor.capture());
    Product product = productCaptor.getValue();

    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getSku()).isEqualTo("safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

}
