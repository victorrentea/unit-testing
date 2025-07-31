package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.service.ProductMapper;
import victor.testing.spring.service.ProductService;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

class GetProductSolitaryTest {
  @Test
  void whatAmITestingHere() {
    // DON'T DO THIS: mocking the mapper out
    ProductRepo repoMock = mock(ProductRepo.class);
    Product product = new Product().setSupplier(new Supplier());
    when(repoMock.findById(1L)).thenReturn(Optional.of(product));
    ProductMapper mapperMock = mock(ProductMapper.class);
    ProductDto dto = ProductDto.builder().build();
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
            .setBarcode("BARCODE")
            .setCategory(HOME)
            .setCreatedDate(date)
            .setSupplier(new Supplier().setCode("S"));

    ProductDto dto = new ProductMapper().toDto(product);

    assertThat(dto.id()).isEqualTo(1L);
    assertThat(dto.name()).isEqualTo("name");
    assertThat(dto.barcode()).isEqualTo("BARCODE");
    assertThat(dto.category2()).isEqualTo(HOME);
    assertThat(dto.createdDate()).isEqualTo(date);
    assertThat(dto.supplierCode()).isEqualTo("S");
  }
}
