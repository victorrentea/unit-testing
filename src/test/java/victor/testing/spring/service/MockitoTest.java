package victor.testing.spring.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "debug=true")
//@ActiveProfiles("db-mem")
@Testcontainers

@Transactional // the best for RDB interaction
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // for testing spring extensions
public class MockitoTest {

   @Container
   static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
           .withDatabaseName("prop")
           .withUsername("postgres")
           .withPassword("password");

   @SneakyThrows
   @DynamicPropertySource
   public static void registerPgProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
      registry.add("spring.datasource.driver-class-name", ()-> "com.p6spy.engine.spy.P6SpyDriver");
      registry.add("spring.datasource.username", postgres::getUsername);
      registry.add("spring.datasource.password", postgres::getPassword);
   }

   @MockBean
   public SafetyClient mockSafetyClient;// = new SafetyClient(new RestTemplate());
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @BeforeEach
   final void before() {
       //cleanup mongo
   }

   @Test
   public void createThrowsForUnsafeProduct() {
//      when(mockSafetyClient.isSafe("bar")).thenReturn(false);

      Assertions.assertThrows(IllegalStateException.class, () ->
          productService.createProduct(new ProductDto("name", "bar", -1L, ProductCategory.HOME)));
   }

   @Test
   public void createOk() {
      // GIVEN
//      Supplier supplier = new Supplier().setId(13L);
//      when(supplierRepo.getById(supplier.getId())).thenReturn(supplier);
      Supplier supplier = supplierRepo.save(new Supplier());
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      // WHEN
      Long createdId = productService.createProduct(new ProductDto("name", "safebar", supplier.getId(), ProductCategory.HOME));

      // THEN
//      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
//      verify(productRepo).save(productCaptor.capture());
//      Product product = productCaptor.getValue();
//      assertThat(productRepo.findById(createdId)).hasSize(1);
      Product product = productRepo.findById(createdId).get();

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }
   @Test
   public void createOkBis() {
      // GIVEN
//      Supplier supplier = new Supplier().setId(13L);
//      when(supplierRepo.getById(supplier.getId())).thenReturn(supplier);
      Supplier supplier = supplierRepo.save(new Supplier());
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);

      // WHEN
      productService.createProduct(new ProductDto("name", "safebar", supplier.getId(), ProductCategory.HOME));

      // THEN
//      ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
//      verify(productRepo).save(productCaptor.capture());
//      Product product = productCaptor.getValue();
      assertThat(productRepo.findAll()).hasSize(1);
      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
      assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
   }

}
