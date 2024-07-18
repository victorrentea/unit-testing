//package victor.testing.design.social;
//
//import org.junit.jupiter.api.Test;
//import victor.testing.spring.entity.Product;
//import victor.testing.spring.entity.Supplier;
//import victor.testing.spring.repo.ProductRepo;
//import victor.testing.spring.rest.dto.ProductDto;
//import victor.testing.spring.service.ProductMapper;
//import victor.testing.spring.service.ProductService;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static victor.testing.spring.entity.ProductCategory.HOME;
//
//public class GetProductSocialTest {
//  private ProductRepo productRepo = mock(ProductRepo.class);
//  private ProductService productService = new ProductService(
//      null,
//      productRepo,
//      null,
//      new ProductMapper(),
//      null);
//
//  @Test
//  void service_plus_mapper() {
//    LocalDate date = LocalDate.now();
//    Product product = new Product()
//            .setId(1L)
//            .setName("name")
//            .setBarcode("BARCODE")
//            .setCategory(HOME)
//            .setCreatedDate(date)
//            .setSupplier(new Supplier().setCode("S"));
//    when(productRepo.findById(1L)).thenReturn(Optional.of(product));
//
//    ProductDto dto = productService.getProduct(1L);
//
//    assertThat(dto.getId()).isEqualTo(1L);
//    assertThat(dto.getName()).isEqualTo("name");
//    assertThat(dto.getBarcode()).isEqualTo("BARCODE");
//    assertThat(dto.getCategory()).isEqualTo(HOME);
//    assertThat(dto.getCreatedDate()).isEqualTo(date);
//    assertThat(dto.getSupplierCode()).isEqualTo("S");
//  }
//
//}
