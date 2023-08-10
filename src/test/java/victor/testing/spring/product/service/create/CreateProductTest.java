package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductMapper;
import victor.testing.spring.product.service.ProductService;
import victor.testing.spring.product.api.dto.ProductDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;

// test slice does not start all spring but only 10% of it
//@WebFluxTest// REST endpoints reactive
//@DataMongoTest // mongo query
//@WebAuthorizationTest // Pincic specific to only test @Secured

//@Testcontainers // a java lib connecting the JVM process with the docker daemon on the OS, telling it to start images.
@SpringBootTest // you boot up everything (the whole spring, 200 beans at least and 100+ autoconfiguration)
public class CreateProductTest extends IntegrationTest {
  public static final String PRODUCT_NAME = "name";
  @MockBean // put a mockito mock in Spring instead of the real bean implementation
  SafetyClient safetyClient;

  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;// = mock(KafkaTemplate.class);

  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;

  @Autowired
  ProductService productService;// = new ProductService(
  // safetyClient :
  // a) use the expections.json as you do in Component Tests
  // b) manually program a MockServer writing java code
  // c) mock the bean (Pro: you do not have to create JSON responses of external APIs)

  //  productRepo, supplierRepo, new ProductMapper(), kafkaTemplate);

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyClient.isSafe("unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto(PRODUCT_NAME, "unsafe", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe: unsafe");
  }

  @Test
  void createOk() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();// Test Data Factory (SupplierData.aSupplier())
    when(safetyClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto(PRODUCT_NAME, "safe", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
    Product product = productRepo.findByName(PRODUCT_NAME);
    assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
    assertThat(product.getSku()).isEqualTo("safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    // assertThat(product.getCreateDate()).isToday(); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }


  @Test
  void missingCategoryDefaultsToUNCATEGORIZED() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();// Test Data Factory (SupplierData.aSupplier())
    when(safetyClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto(PRODUCT_NAME, "safe", supplierId, null);

    productService.createProduct(dto);

    Product product = productRepo.findByName(PRODUCT_NAME);
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
