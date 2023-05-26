package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;

// the db spring/hibernate needs can be:
// - in-mem H2
// - in a Docker just for tests (@Testcontainers ftw)
@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional // every @Test runs in its own transaction, ROLLEDBACK automatically after each test
  // NOT working if 1) you use @Transactional(propagation=REQUIRES_NEW) in prod 2) you test multithreaded code 3) you do Transaction.start yoursef
//@Sql(scripts = "classpath:/sql/cleanup.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // for terrible PL/SQL database
@Sql(scripts = "classpath:/sql/common-reference-data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // for terrible PL/SQL database
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // NEVER PUSH THIS ON GIT. only use it if you develop extensions to spring
public class CreateProductTest {
  @MockBean // = creates a Mockito.mock for this type and replaces the real class in the context with this mock
  SafetyClient mockSafetyClient;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

//  @AfterEach
//  @BeforeEach
//  public void cleanupDB() {
//    productRepo.deleteAll(); // in the FK order
//    supplierRepo.deleteAll();
//  }
  @Test
  public void createThrowsForUnsafeProduct() {
    when(mockSafetyClient.isSafe("bar")).thenReturn(false);

    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);
    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void createOk() {
    // GIVEN
    Long supplierId = -1L;//supplierRepo.save(new Supplier()).getId();
    when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
    Product product = productRepo.findAll().get(0);
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
     assertThat(product.getCreateDate()).isNotNull();
  }

  @Test
  public void createOkMIssingCategory() {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, null);

    // WHEN
    productService.createProduct(dto);

    // THEN
    Product product = productRepo.findAll().get(0);
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}
