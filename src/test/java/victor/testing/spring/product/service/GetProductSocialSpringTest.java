package victor.testing.spring.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductMapper;
import victor.testing.spring.product.service.ProductService;
import victor.testing.spring.product.api.dto.ProductDto;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;

@ExtendWith(SpringExtension.class)
@Import({ProductService.class, ProductMapper.class})
public class GetProductSocialSpringTest {
  @MockBean
  SafetyClient safetyClient;
  @MockBean
  private ProductRepo productRepo;
  @MockBean
  private SupplierRepo supplierRepo;
  @Autowired
  private ProductService productService;

  @Test
  void service_plus_mapper() {
    LocalDate date = LocalDate.now();
    Product product = new Product()
            .setId(1L)
            .setName("name")
            .setBarcode("bar")
            .setCategory(HOME)
            .setCreateDate(date)
            .setSupplier(new Supplier().setId(2L));
    when(productRepo.findById(1L)).thenReturn(Optional.of(product));

    ProductDto dto = productService.getProduct(1L);

    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getName()).isEqualTo("name");
    assertThat(dto.getBarcode()).isEqualTo("bar");
    assertThat(dto.getCategory()).isEqualTo(HOME);
    assertThat(dto.getSupplierId()).isEqualTo(2L);
  }

}
