package victor.testing.spring.service;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
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
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

//@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:test")
@ActiveProfiles("test")// sa incarc application.test.properties
@EmbeddedKafka // buteaza o kafka in memorie ~ H2
@SpringBootTest // porneste springu in procesul JUNit

// #2 pt schema legacy (multe tabele)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class CreateProductTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockBean
  SafetyApiAdapter safetyApiAdapter;
  @MockBean // inlocuieste in spring context instanta reala cu un mock mockito
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;

//  @BeforeEach // #1 programatic inainte si dupa test
//  @AfterEach
//  final void setup() {
//    productRepo.deleteAll(); // cu grije in ordinea FKurilor
//    supplierRepo.deleteAll();
////    cacheManager.getCache()..clear()
//    //wireMOck.reset();
//  }

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);
    ProductDto productDto = new ProductDto("name", "barcode-unsafe", "S", HOME);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);

    // WHEN
    var id = productService.createProduct(productDto);

    Product product = productRepo.findById(id).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        argThat(e -> e.productId().equals(id)));
  }
  @Test
  void defaultsToUncategorizedForWithMissingCategory() {
    supplierRepo.save(new Supplier().setCode("S"));
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
    ProductDto productDto = new ProductDto(
        "name", "barcode-safe", "S", null);

    // WHEN
    var id = productService.createProduct(productDto);

    Product product = productRepo.findById(id).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }
}