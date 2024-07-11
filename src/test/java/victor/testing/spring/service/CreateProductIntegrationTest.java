package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.BaseIntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Product;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static victor.testing.spring.domain.ProductCategory.HOME;
import static victor.testing.spring.domain.ProductCategory.UNCATEGORIZED;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@WithMockUser(roles = "ADMIN", username = "jdoe")
@AutoConfigureWireMock(port = 8089)
@TestPropertySource(properties = "safety.service.url.base=http://localhost:8089")

// #4 - avoid! = nukes Spring context after each test method, cau
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class CreateProductIntegrationTest extends BaseIntegrationTest {
  public static final String BARCODE = "barcode-safe";
  public static final String PRODUCT_NAME = "name";
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductService service;

  ProductDto dto = new ProductDto()
      .setBarcode(BARCODE)
      .setSupplierCode(SUPPLIER_CODE)
      .setName(PRODUCT_NAME)
      .setCategory(HOME);

  @BeforeEach
  final void setup() {
    // SAML/Oauth integration => (custom library) SecurityUtils.getUsername(), getRole()
    // ideally that lib should also have a test support function
//    SecurityContextHolder.setContext(
//        SecurityTestUtils.createContextJustLikeInProdButForProduction("jdoe", "ADMIN", "phonenumber"));

    Object whatTypeIsThis = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // can i instantiate this class myself?
//    eg:OAuthUser// << how can i instantiate this class myself?
//    SecurityContextHolder.setContext(new SecurityContextImpl(new PreAuthenticatedAuthenticationToken(oAuthUser)));
  }
  @Test
  void failsForUnsafeProduct() {
    // programmatic config of mock responses from
//    stubFor(get("/product/barcode-unsafe/safety")
//        .willReturn(okJson("""
//            {
//             "category": "NOT SAFE",
//             "detailsUrl": "http://details.url/a/b"
//            }
//            """)));
    dto.setBarcode("barcode-unsafe");

    assertThatThrownBy(() -> service.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  @WithMockUser( roles = "USER")
  void regularUserIsDeniedAccess() {
    assertThatThrownBy(()->service.createProduct(dto))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void savesTheProduct() {
    // prod call
    Long productId = service.createProduct(dto);

    Product product = productRepo.findById(productId).get();
    // SHOCK: this does not trigger an SELECT but retrieves product from JPA 1st level cache
    assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
    assertThat(product.getBarcode()).isEqualTo(BARCODE);
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getSupplier().getCode()).isEqualTo(SUPPLIER_CODE);
    assertThat(product.getCreatedDate()).isToday();
    assertThat(product.getCreatedBy()).isEqualTo("jdoe");
  }

  @Test
  void sendsKafkaMessage() {
    service.createProduct(dto);

    verify(kafkaTemplate).send(PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

  @Test
  void defaultsCategoryToUncategorized() {
//    when(safetyApiClient.isSafe(BARCODE)).thenReturn(true);
    dto.setCategory(null);

    long productId = service.createProduct(dto);

    Product product = productRepo.findById(productId).get();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

}