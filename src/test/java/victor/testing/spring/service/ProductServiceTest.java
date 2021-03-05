package victor.testing.spring.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

   @InjectMocks
   private ProductService service;
   @Mock
   private SafetyClient safetyClient;
   @Mock
   private SupplierRepo supplierRepo;

   @Test(expected = IllegalStateException.class)
   public void unsafeProduct() {
      ProductDto dto = new ProductDto().setUpc("upc");
      Mockito.when(safetyClient.isSafe("upc")).thenReturn(false);
      service.createProduct(dto);
   }

   @Test(expected = IllegalArgumentException.class)
   public void throwsForEmptyName() {
      ProductDto dto = new ProductDto().setName(null);
      Mockito.when(safetyClient.isSafe(any())).thenReturn(true);
      service.createProduct(dto);
   }

   @Test
   public void createsCorrectEntity() {
      Supplier aSupplier = new Supplier();
      Mockito.when(supplierRepo.findById(15L)).thenReturn(Optional.of(aSupplier));
      ProductDto dto = new ProductDto()
          .setName("name")
          .setCategory(ProductCategory.KIDS)
          .setUpc("upc")
          .setSupplierId(15L);
      Mockito.when(safetyClient.isSafe(any())).thenReturn(true);

      service.createProduct(dto);

   // TODO captor TEMA
   }
}