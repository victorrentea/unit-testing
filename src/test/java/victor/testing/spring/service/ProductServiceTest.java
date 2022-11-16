package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.TestcontainersUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.*;

// destroy the spring context before each test method => spring starts over again->
// // HERE IS DOES NO DESTROY THE DATABASE CONTENTS: flyway sees the DB up-to-date, even if there is garbage data from the prev test class.
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // git hook to reject pushes to remote if they contained @DirtiesContext
// => you only use this on your machine to find out if the test coupling si due to something in Spring.


@WithWiremock
public class ProductServiceTest extends BaseTest {
   //   @MockBean // this tells Spring to REPLACE in its context the real SafetyClient bean with a Mockit mock!
   //   // and injhect that mock in this field to allow you teach its methods what to return
   //   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;

   @Autowired
   private ProductService productService;
   //   @BeforeEach
   //   @AfterEach
   //   public void teardown() {
   //      productRepo.deleteAll();
   //   }

   // best solution for non-transacted databases,
   // clean kafaka listeners, clear caches,
   // delete files

   //   @Test
   //   public void createThrowsForUnsafeProduct() {
   //      when(mockSafetyClient.isSafe("bar")).thenReturn(false);
   //      ProductDto dto = new ProductDto("name", "bar", -1L, HOME);
   //
   //      assertThatThrownBy(() -> productService.createProduct(dto))
   //              .isInstanceOf(IllegalStateException.class);
   //   }


   @Test
   public void createOk() {
      // GIVEN
      //      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      List<Product> products = productRepo.findAll();
      assertThat(products).hasSize(1);
      Product product = products.get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

   @Test
   public void createOkBis() {
      // GIVEN
      //      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", supplierId, HOME);

      // WHEN
      productService.createProduct(dto);

      // THEN
      List<Product> products = productRepo.findAll();
      assertThat(products).hasSize(1);
      Product product = products.get(0);
      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
