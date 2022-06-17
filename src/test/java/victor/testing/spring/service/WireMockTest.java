//package victor.testing.spring.service;
//
//import com.github.tomakehurst.wiremock.client.WireMock;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.RegisterExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import victor.testing.spring.domain.Product;
//import victor.testing.spring.domain.ProductCategory;
//import victor.testing.spring.domain.Supplier;
//import victor.testing.spring.repo.ProductRepo;
//import victor.testing.spring.repo.SupplierRepo;
//import victor.testing.spring.web.dto.ProductDto;
//import victor.testing.tools.WireMockExtension;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@SpringBootTest(properties = "safety.service.url.base=http://localhost:8089")
//@ActiveProfiles("db-mem")
//@Transactional
//public class WireMockTest {
//   @Autowired
//   private ProductRepo productRepo;
//   @Autowired
//   private SupplierRepo supplierRepo;
//   @Autowired
//   private ProductService productService;
//
//   @RegisterExtension // starts-stops the WireMock web server that replies with pre-configured JSON responses
//   public WireMockExtension wireMock = new WireMockExtension(8089);
//
//   @Test
//   public void throwsForUnsafeProduct() {
//      assertThatThrownBy(() -> {
//         productService.createProduct(new ProductDto("name", "bar", -1L, ProductCategory.HOME));
//      }).isInstanceOf(IllegalStateException.class);
//   }
//
//   @Test
//   public void programmatic_wiremock() {
//      WireMock.stubFor(get(urlEqualTo("/product/customXX/safety"))
//          .willReturn(aResponse()
//              .withStatus(200)
//              .withHeader("Content-Type", "application/json")
//              .withBody("{\"entries\": [{\"category\": \"DETERMINED\",\"detailsUrl\": \"http://wikipedia.com\"}]}"))); // override
//
//      assertThatThrownBy(() ->
//              productService.createProduct(new ProductDto("name", "customXX", -1L, ProductCategory.HOME))).isInstanceOf(IllegalStateException.class);
//   }
//
//   @Test
//   public void fullOk() {
//      long supplierId = supplierRepo.save(new Supplier()).getId();
//      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);
//
//      productService.createProduct(dto);
//
//      Product product = productRepo.findAll().get(0);
//      assertThat(product.getName()).isEqualTo("name");
//      assertThat(product.getBarcode()).isEqualTo("safebar");
//      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
//      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
//      assertThat(product.getCreateDate()).isNotNull();
//   }
//}
