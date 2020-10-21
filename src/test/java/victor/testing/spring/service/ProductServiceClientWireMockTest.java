package victor.testing.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import lombok.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.web.dto.ProductDto;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.FileReader;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "safety.service.url.base=http://localhost:9999",
    "spring.main.allow-bean-definition-overriding=true"})
@ActiveProfiles("db-mem")
public class ProductServiceClientWireMockTest {
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
//      wireMock.reset
      // Clear manually all caches
      cacheManager.getCacheNames().stream().map(cacheManager::getCache).forEach(Cache::clear);
   }

   @Test
   public void throwsForUnsafeProduct() {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         productService.createProduct(new ProductDto("name", "UNSAFE", -1L, ProductCategory.HOME));
      });
   }

   @Value
   static class Entry {
      String category;
      String detailsUrl;
   }
   @Value
   static class Entries {
      List<Entry> entries;
   }

   @Test
   public void throwsForUnsafeProductProgrammaticWireMock() throws JsonProcessingException {
      Assertions.assertThrows(IllegalStateException.class, () -> {
         productService.createProduct(new ProductDto("name", "UNSAFE", -1L, ProductCategory.HOME));
      });
   }


   @Test
   public void fullOk() {
      long supplierId = supplierRepo.save(new Supplier()).getId();

      ProductDto dto = new ProductDto("name", "1", supplierId, ProductCategory.HOME);
      LocalDateTime startTime = LocalDateTime.now();
      productService.createProduct(dto);

      Product product = productRepo.findAll().get(0);
//      LocalDateTime today = LocalDateTime.parse("2014-12-22T10:15:30.00");

      assertThat(product.getName()).isEqualTo("name");
      assertThat(product.getUpc()).isEqualTo("1");
      assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
      assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);

      // pica din cand in cand, dar doar pe Jenkins
//      assertThat(product.getCreateDate()).isEqualTo(LocalDateTime.now());

//      assertThat(product.getCreateDate()).isNotNull();

      // inginerul siktirit
      assertThat(product.getCreateDate()).isNotNull();

      // ingineru mai cu carte
      assertThat(product.getCreateDate()).isAfterOrEqualTo(startTime);

   }

//   @MockBean
//   Clock clock;


   @Test
   public void test() {
      // <<< fixez timpul in 2019
      // create prin API public fara sa umbli in baza
      // <<-- avansez timpul cu 1 an.
      // search prin searchProduct
      //     Asta e limitare
   }

   @TestConfiguration
   public static class TestConfig {
      @Bean
      @Primary
      public Clock clock() {
         return Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"),
             ZoneId.systemDefault());
      }
   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
