package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.service.GetProductService;
import victor.testing.spring.service.ProductMapper;
import victor.testing.spring.service.ProductService;

import java.time.LocalDate;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

@ExtendWith(MockitoExtension.class)
class GetProductSolitaryTest {
  public static final String NAME = "PROD_NAME";
  private Product product = new Product().setName(NAME).setSupplier(new Supplier());
  @Mock
  ProductRepo repoMock;
  @Mock
  ProductMapper mapperMock;
  @InjectMocks
  GetProductService productService;

  @Test
  void whatAmITestingHere() {
    // DON'T DO THIS: mocking the mapper out
    when(repoMock.findById(1L)).thenReturn(Optional.of(this.product));
    ProductDto dto = ProductDto.builder().build();
    when(mapperMock.toDto(this.product)).thenReturn(dto);
    when(repoMock.countByName(NAME)).thenReturn(0);


    ProductDto returnedDto = productService.getProduct(1L);

//    Mockito.verify(repoMock).countByName(any());
    assertThat(returnedDto).isSameAs(dto);
  }
  @Test
  void throwsForExistingProductByName() {
    // DON'T DO THIS: mocking the mapper out
    Product product = this.product;

    when(repoMock.findById(1L)).thenReturn(Optional.of(product));
    ProductDto dto = ProductDto.builder().build();
    // when(mapperMock.toDto(product)).thenReturn(dto);
    when(repoMock.countByName(NAME)).thenReturn(1);

    assertThatThrownBy(() -> productService.getProduct(1L))
        .isInstanceOf(IllegalStateException.class);
  }

//  @Test
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
    assertThat(dto.category()).isEqualTo(HOME);
    assertThat(dto.createdDate()).isEqualTo(date);
    assertThat(dto.supplierCode()).isEqualTo("S");
  }
}
