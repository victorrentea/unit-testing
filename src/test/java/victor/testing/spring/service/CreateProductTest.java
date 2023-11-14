package victor.testing.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
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

@Slf4j
@SpringBootTest
@ActiveProfiles("db-mem") // H2 in-memory
public class CreateProductTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockBean
  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductService productService;

  @Test
  void createThrowsForUnsafeProduct() {
    // programezi mockul ce sa raspunda
    when(safetyClient.isSafe("upc-unsafe"))
        .thenReturn(false);
    ProductDto dto = new ProductDto("name", "upc-unsafe", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(safetyClient.isSafe("upc-safe")).thenReturn(true);// INSERT
    ProductDto dto = new ProductDto("name", "upc-safe", supplierId, HOME);

    // "WHEN" = call to code under test
    productService.createProduct(dto);

    log.info("THEN: verificarile de dupa");
    Product product = productRepo.findByName("name").get(0); // SELECT
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getUpc()).isEqualTo("upc-safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    //assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    //assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic

    // intreaba mockul daca s-a chemat metoda #send
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

}
