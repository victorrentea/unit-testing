package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

//@ExtendWith(MockitoExtension.class)

// @RunWith(SpringRunner.class) // 4
@SpringBootTest
@ActiveProfiles("db-mem-migration")
public class ProductServiceTest {
  @MockBean // inlocuieste un bean de spring cu un mockito mock pe care-l si pune aici sa-l programezi
  public SafetyClient mockSafetyClient;
  @Autowired
  private ProductRepo productRepo;
  @Autowired
  private SupplierRepo supplierRepo;
  @Autowired
  private ProductService productService;

  @BeforeEach
  final void before() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }

  @Test
  public void createThrowsForUnsafeProduct() {
    when(mockSafetyClient.isSafe("bar")).thenReturn(false);

    ProductDto dto = new ProductDto("name", "bar", -1L, HOME);
    assertThatThrownBy(() -> productService.createProduct(dto))
            .isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void createOk() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    productService.createProduct(dto);

    List<Product> products = productRepo.findAll();
    assertThat(products).hasSize(1);
    Product product = products.get(0);
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS)); // uses Spring Magic
  }
  @Test
  public void createOkBis() {
    Long supplierId = supplierRepo.save(new Supplier()).getId();
    when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

    productService.createProduct(dto);

    List<Product> products = productRepo.findAll();
    assertThat(products).hasSize(1);
    Product product = products.get(0);
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("safebar");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS)); // uses Spring Magic
  }

}
