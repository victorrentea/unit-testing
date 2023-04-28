package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductDto;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

@SpringBootTest
@Import({ProductService.class})
public class GetProductSocialSpTest {
  @MockBean
  private ProductRepo productRepo;
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
    assertThat(dto.getCreateDate()).isEqualTo(date);
    assertThat(dto.getSupplierId()).isEqualTo(2L);
  }

}