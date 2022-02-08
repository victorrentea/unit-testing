package victor.testing.spring.service.subpackage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.WireMockExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // slice tests. speeds up this test run alone, but if you run all of them, it will cause a second spring context to start.

//@SpringBootTest(properties = "safety.service.url.base=http://localhost:8089", webEnvironment = WebEnvironment.NONE)
@ActiveProfiles({"db-mem","profile1"})
@Transactional
public class ProductServiceWireMock2Test {
   @Autowired
   public SafetyClient mockSafetyClient;
   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductService productService;

   @RegisterExtension
   public WireMockExtension wireMock = new WireMockExtension(8089);
   @Autowired
   private CacheManager cacheManager;

   @BeforeEach
   public void initialize() {
      // Clear manually all caches
      cacheManager.getCacheNames().stream().map(cacheManager::getCache).forEach(Cache::clear);
   }

   @Test
   public void throwsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         productService.createProduct(new ProductDto("name", "bar", -1L, ProductCategory.HOME));
      });
   }

   @Test
   public void throwsForUnsafeProductProgrammaticWireMock() {
//      Assertions.assertThrows(IllegalStateException.class, () -> {
//         String template;
//         try (FileReader reader = new FileReader("C:\\workspace\\integration-testing-spring\\src\\test\\java\\victor\\testing\\spring\\facade\\inTemplate.json")) {
//            template = IOUtils.toString(reader);
//         }
//         WireMock.stubFor(get(urlEqualTo("/product/customXX/safety"))
//             .willReturn(aResponse()
//                 .withStatus(200)
//                 .withHeader("Content-Type", "application/json")
//                 .withBody("{\"entries\": [{\"category\": \"DETERMINED\",\"detailsUrl\": \"http://wikipedia.com\"}]}"))); // override
//
//         productService.createProduct(new ProductDto("name", "customXX", -1L, ProductCategory.HOME));
//      });
   }

   @Test
   public void fullOk() {
      long supplierId = supplierRepo.save(new Supplier()).getId();

      ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);
      productService.createProduct(dto);

      Product product = productRepo.findAll().get(0);

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getBarcode()).isEqualTo("safebar");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);

      assertThat(product.getCreateDate()); // TODO test time
   }

//   @TestConfiguration
//   public static class TestConfig {
//      @Bean
//      @Primary
//      public Clock clockFixed() {
//         return Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.systemDefault());
//      }
//   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
