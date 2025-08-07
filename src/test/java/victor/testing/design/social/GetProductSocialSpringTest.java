package victor.testing.design.social;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.service.GetProductService;
import victor.testing.spring.service.ProductMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;

@ExtendWith(SpringExtension.class)
// use spring ONLY as DI without booting web/hibernate/acutator/+1000 other defautl beans
@ContextConfiguration(classes = {
    GetProductService.class, //real
    ProductMapper.class //real
})
@TestPropertySource(properties = "prop.from.file=1")
class GetProductSocialSpringTest {
  public static final String NAME = "PROD_NAME";
  private static final LocalDate CREATED_DATE = LocalDate.now();
  @MockitoBean
  ProductRepo productRepoMock; // mock
  @Autowired
  GetProductService productService;

  Product product = new Product()
      .setId(1L)
      .setName(NAME)
      .setSupplier(new Supplier().setCode("S"))
      .setBarcode("BARCODE")
      .setCategory(HOME)
      .setCreatedDate(CREATED_DATE);
  @Test
  void happy() {
    when(productRepoMock.findById(1L)).thenReturn(Optional.of(product));
    when(productRepoMock.countByName(NAME)).thenReturn(0);

    ProductDto dto = productService.getProduct(1L);

    assertThat(dto.id()).isEqualTo(1L);
    assertThat(dto.name()).isEqualTo(NAME);
    assertThat(dto.barcode()).isEqualTo("BARCODE");
    assertThat(dto.category()).isEqualTo(HOME);
    assertThat(dto.createdDate()).isEqualTo(CREATED_DATE);
    assertThat(dto.supplierCode()).isEqualTo("S");
  }
  @Test
  void throwsForExistingProductByName() {
    when(productRepoMock.findById(1L)).thenReturn(Optional.of(product));
    when(productRepoMock.countByName(NAME)).thenReturn(1);

    assertThatThrownBy(() -> productService.getProduct(1L))
        .isInstanceOf(IllegalStateException.class);
  }
}
