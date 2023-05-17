package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductDto;

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
    ProductService productService = new ProductService(null, repoMock, null, mapperMock);

    ProductDto returnedDto = productService.getProduct(1L);

    assertThat(returnedDto).isSameAs(dto);
  }

  @Test
  void mapperTest() {
    LocalDate date = LocalDate.now();
    Product product = new Product()
            .setId(1L)
            .setName("name")
            .setBarcode("bar")
            .setCategory(HOME)
            .setCreateDate(date)
            .setSupplier(new Supplier().setId(2L));

    ProductDto dto = new ProductMapper().toDto(product);

    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getName()).isEqualTo("name");
    assertThat(dto.getBarcode()).isEqualTo("bar");
    assertThat(dto.getCategory()).isEqualTo(HOME);
    assertThat(dto.getCreateDate()).isEqualTo(date);
    assertThat(dto.getSupplierId()).isEqualTo(2L);
  }
}