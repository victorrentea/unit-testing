package victor.testing.spring.service;

import lombok.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.api.dto.ProductDto;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;

@ActiveProfiles("test")
@SpringBootTest // test de integrare cu spring pornit

//@Transactional // #3 daca o pui in teste, spring intelege sa faca auto-rollback
// si tranzactia de test ("fantomitza rosie") se propga catre codul testat
// NU acopera flush()->UQ/NOT NULL/FK violation
// NU merge daca procedura face COMMIT in ea
// NU Merge daca codul testat e @Async sau @Transactional(propagation = REQUIRES_NEW)

// #2 sql manual de insert/cleanup
//@Sql(value = "/sql/cleanup.sql",executionPhase = BEFORE_TEST_METHOD)

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD) // ANTI-PATTERN
public class CreateProductTest {
  @Autowired // inlocuieste bean-ul real cu un mock Mockito in Spring
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockBean
  SafetyClient safetyClient;
  @Autowired
  ProductService productService;
  private @NonNull ProductDto aProduct(String supplierName) {
    return new ProductDto("name", "upc-safe", supplierName, HOME);
  }

  @AfterEach
  public void trageApa() {
    productRepo.flush(); // flush nu inseamna commit.
  }
  //#1 manual delete
//  @BeforeEach
//  @AfterEach
//  public void cleanDB() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll(); // nuschimba ordinea
//  }

  @Test
//  @Sql
  void createThrowsForUnsafeProduct() {
    when(safetyClient.isSafe("upc-unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "upc-unsafe", "-1L", HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  @WithMockUser(username = "user")
  void createOk() {
    long supplierId = supplierRepo.save(new Supplier().setName("S")).getId(); // ca pe bune in realitate
    when(safetyClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto dto = aProduct("S");

    // WHEN
    long productId = productService.createProduct(dto);

    Product product = productRepo.findById(productId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getUpc()).isEqualTo("upc-safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
  }

  @Test
  void createOkCuCategoriaNull() {
    long supplierId = supplierRepo.save(new Supplier().setName("S")).getId(); // ca pe bune in realitate
    when(safetyClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto dto = aProduct("S").setCategory(null);

    long productId = productService.createProduct(dto);

    Product product = productRepo.findById(productId).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }
}
