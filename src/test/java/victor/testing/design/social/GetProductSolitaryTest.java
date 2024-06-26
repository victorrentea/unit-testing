package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.service.ProductMapper;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.api.dto.ProductDto;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.HOME;

public class GetProductSolitaryTest {
  @Test
  void whatAmITestingHere() {
    // DON'T DO THIS: mocking the mapper out
    ProductRepo repoMock = mock(ProductRepo.class);
    Product product = new Product().setSupplier(new Supplier());
    when(repoMock.findById(1L)).thenReturn(Optional.of(product));
    ProductMapper mapperMock = mock(ProductMapper.class);
    ProductDto dto = new ProductDto();
    when(mapperMock.toDto(product)).thenReturn(dto);
    ProductService productService = new ProductService(null, repoMock, null, mapperMock, null);

    ProductDto returnedDto = productService.getProduct(1L);

    assertThat(returnedDto).isSameAs(dto);
  }

  @Test
  void mapperTest() {
    LocalDate date = LocalDate.now();
    Product product = new Product()
            .setId(1L)
            .setName("name")
            .setUpc("UPC")
            .setCategory(HOME)
            .setCreatedDate(date)
            .setSupplier(new Supplier().setCode("S"));

    ProductDto dto = new ProductMapper().toDto(product);

    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getName()).isEqualTo("name");
    assertThat(dto.getUpc()).isEqualTo("UPC");
    assertThat(dto.getCategory()).isEqualTo(HOME);
    assertThat(dto.getCreatedDate()).isEqualTo(date);
    assertThat(dto.getSupplierCode()).isEqualTo("S");
  }
}
