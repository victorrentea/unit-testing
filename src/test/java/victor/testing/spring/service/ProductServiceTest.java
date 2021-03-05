package victor.testing.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.RepoTestBase;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class ProductServiceTest extends RepoTestBase {

   @Autowired
   private ProductService service;
   @MockBean
   private SafetyClient safetyClient;
   @Autowired
   private ProductRepo productRepo;

   @Test//(expected = IllegalStateException.class)
   public void unsafeProduct() {
      ProductDto dto = new ProductDto().setUpc("upc");
      Mockito.when(safetyClient.isSafe("upc")).thenReturn(false);
      assertThrows(IllegalStateException.class,
          () -> service.createProduct(dto));
   }

   @Test//(expected = IllegalArgumentException.class)
   public void throwsForEmptyName() {
      ProductDto dto = new ProductDto().setName(null);
      Mockito.when(safetyClient.isSafe(any())).thenReturn(true);
      assertThrows(IllegalArgumentException.class,
      () -> service.createProduct(dto));
   }

   @Test
   public void createsCorrectEntity() {
      ProductDto dto = new ProductDto()
          .setName("name")
          .setCategory(ProductCategory.KIDS)
          .setUpc("upc")
          .setSupplierId(supplier.getId());
      Mockito.when(safetyClient.isSafe(any())).thenReturn(true);

      long id = service.createProduct(dto);

      Product product = productRepo.findById(id).get();

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getCategory()).isEqualTo(ProductCategory.KIDS);
      assertThat(product.getUpc()).isEqualTo("UPC");
      assertThat(product.getSupplier().getId()).isEqualTo(supplier.getId());
   }
}