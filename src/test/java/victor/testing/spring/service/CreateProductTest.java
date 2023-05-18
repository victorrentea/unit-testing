package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

//@ActiveProfiles("could") // strica cacheul, nu poate spring refolosi contextul de dinainte pentru tine
//@TestPropertySource(properties = "prop=#sieu")// strica cacheul, nu poate spring refolosi contextul de dinainte pentru tine
public class CreateProductTest extends BaseFunctionalTest {
   public static final long SUPPLIER_ID = 13L;
   @MockBean
   public SafetyClient mockSafetyClient;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;


   @Test
   public void uituc() {
      // GIVEN
       supplierRepo.save(new Supplier().setId(SUPPLIER_ID));
      when(mockSafetyClient.isSafe("safebar")).thenReturn(true);
      ProductDto dto = new ProductDto("name", "safebar", SUPPLIER_ID, HOME);

      // WHEN
      productService.createProduct(dto);
   }

}
