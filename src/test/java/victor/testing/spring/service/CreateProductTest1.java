package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
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
import java.util.Random;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.HOME;

//@SpringBootTest
//@ActiveProfiles("db-migration")
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // doamne fereste sa push asa ceva. pierzi timp de build.
// niciodata decat ca sa debugezi probleme (investigare) sau
// doar daca testezi EXTENSII LA SPRING
public class CreateProductTest1 extends BaseFunctionalTest {
   public final long SUPPLIER_ID = 13;// new Random().nextLong();
   @MockBean
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @AfterEach
   @BeforeEach
   public void method() {
      // 1 spring to rule them all, dar nu multithreaded friendly
      productRepo.deleteAll();
      try {
         supplierRepo.deleteAllById(List.of(SUPPLIER_ID));
      } catch (Exception ignored) {

      }
   }

   // cum previi data leaks intre teste functional (izolezi testele)
   // 1) delete all in before + after -nu parallelizare
   // 2) toti sa citeasca datele lor dupa criterii unice: "name" + UUID.randomUUID()
   // 3) sa le si stergi la final 😇😇😇😇😇


   // by default embedded-containers NU fac reuse 😱
   // spring context e unu pentru toata clasa (poti sa o strici cu @Dirties Context)
   @Test
   public void acreateThrowsForUnsafeProduct() {
      when(mockSafetyClient.isSafe("bar")).thenReturn(false);

      ProductDto dto = new ProductDto("name", "bar", -1L, HOME);
      assertThatThrownBy(() -> productService.createProduct(dto))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void createOk() {
      // GIVEN
      Supplier supplier = supplierRepo.save(new Supplier().setId(SUPPLIER_ID));
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name" + UUID.randomUUID(), "safebar", SUPPLIER_ID, HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      Product product = productRepo.findByName(dto.name).orElseThrow();
      assertThat(product.getName()).isEqualTo(dto.name);
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(SUPPLIER_ID);
      assertThat(product.getCategory()).isEqualTo(HOME);
      // assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS)); // uses Spring Magic
   }

   @Test
   public void createOkBis() {
      // GIVEN
      Supplier supplier = supplierRepo.save(new Supplier().setId(SUPPLIER_ID));
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", SUPPLIER_ID, HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      Product product = productRepo.findByName(dto.name).orElseThrow();
      assertThat(product.getName()).isEqualTo(dto.name);
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(SUPPLIER_ID);
      assertThat(product.getCategory()).isEqualTo(HOME);
      // assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS)); // uses Spring Magic
   }
}
