package ro.victor.unittest.spring.facade;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.infra.SafetyServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.ProductDto;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductFacadeTest {
   @Autowired
   private ProductFacade facade;
   @MockBean
   private ProductRepo productRepo;
   @MockBean
   private SafetyServiceClient safetyClient;

   @Test(expected = IllegalStateException.class)
   public void throwsForUnsafeProduct() {
      when(productRepo.findById(13L)).thenReturn(of(new Product()));
      facade.getProduct(13L);
   }

   @Test
   public void throwsForInactiveSupplier() {

      when(safetyClient.isSafe(anyString())).thenReturn(true);
      Product product = new Product()
          .setSupplier(new Supplier()
              .setActive(false));
      when(productRepo.findById(13L)).thenReturn(of(product));
      IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
          facade.getProduct(13L));
//      assertTrue(ex.getMessage().contains("supplier"));
      assertThat(ex.getMessage()).containsIgnoringCase("supplier");
   }
//   @Test
//   public void okTODODODODODODODODODODODODODODODODODODODODODODODODODOD() {
//      when(safetyClient.isSafe(anyString())).thenReturn(true);
//      Product product = new Product()
//          .setName("product-name")
//          .setSupplier(new Supplier()
//              .setActive(true));
//      when(productRepo.findById(13L)).thenReturn(of(product));
//      ProductDto dto = facade.getProduct(13L);
//      assertEquals("product-name", dto.productName);
//   }
}
