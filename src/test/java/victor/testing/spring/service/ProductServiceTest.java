package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


// Vlad: dupa fiecare @Test sa fac ROLLBACK la tranzactia pe care am inserat date in DB

@SpringBootTest
@ActiveProfiles("db-mem")
@Execution(ExecutionMode.SAME_THREAD) // !!! Atentie
public class ProductServiceTest {
   @MockBean // inlocuieste beanul real SafetyClient din Spring context
   // cu un mock Mockito, pe care ti-l si injecteaza in campul asta,
   // ca sa-i poti face ce-i faci de ob unui mock. ! intre @Test, behaviorul
   // acestui mock se reseteaza automat
   public SafetyClient safetyClientMock;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @BeforeEach // daca tre sa cureti ceva "sa pleci de pe curat" <<< asta!
      // NU pentru Relational DBs. Asta e buna pentru orice altceva:
   // de golit cozi, cacheuri, mongo, cassandra, unic in Spring. fisiere.
   // (daca joci multithreading pe astea --- esti mort)
   @AfterEach// paranoia?
   public void curataDupaMine() {
      productRepo.deleteAll();
      supplierRepo.deleteAll();
   }
   @Test
   public void createThrowsForUnsafeProduct() {
      when(safetyClientMock.isSafe("bar")).thenReturn(false);

      ProductDto dto = new ProductDto("name", "bar", -1L, ProductCategory.HOME);
      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void vic_createOk() {
      // GIVEN
      // in loc de a "stabui" apelul de repo, fac un "save" inainte de a chema codul testat
      Long supplierId = supplierRepo.save(new Supplier("de ce Doamne?")).getId();
      when(safetyClientMock.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      assertThat(productRepo.findAll()).hasSize(1); // ca tabelele sunt goale la inceput ca e DB in memorie
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

   @Test
   public void vic_createOkBis() {
      // GIVEN
      // in loc de a "stabui" apelul de repo, fac un "save" inainte de a chema codul testat
      Long supplierId = supplierRepo.save(new Supplier("de ce Doamne?")).getId();
      when(safetyClientMock.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      assertThat(productRepo.findAll()).hasSize(1); // ca tabelele sunt goale la inceput ca e DB in memorie
      Product product = productRepo.findAll().get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
