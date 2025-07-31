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

class GetProductSocialTest {
  ProductRepo productRepoMock = mock(ProductRepo.class);
  ProductService productService = new ProductService(
      null,
      productRepoMock,
      null,
      new ProductMapper(), // ala pi bune !!
      null);

  @Test
  void service_plus_mapper() {
    LocalDate date = LocalDate.now();
    Product product = new Product()
        .setId(1L)
        .setName("name")
        .setBarcode("BARCODE")
        .setCategory(HOME)
        .setCreatedDate(date)
        .setSupplier(new Supplier().setCode("S"));
    when(productRepoMock.findById(1L)).thenReturn(Optional.of(product));

    ProductDto dto = productService.getProduct(1L);

    assertThat(dto.id()).isEqualTo(1L);
    assertThat(dto.name()).isEqualTo("name");
    assertThat(dto.barcode()).isEqualTo("BARCODE");
    assertThat(dto.category2()).isEqualTo(HOME);
    assertThat(dto.createdDate()).isEqualTo(date);
    assertThat(dto.supplierCode()).isEqualTo("S");
  }
}
