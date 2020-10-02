package ro.victor.unittest.spring.facade;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.infra.SafetyServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.ProductDto;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductFacadeTest {
   @Autowired
   private ProductFacade facade;
   @MockBean
   private ProductRepo productRepo;
   @MockBean
   private SafetyServiceClient safetyClient;

   @Test(expected = IllegalStateException.class)
   public void throwsForUnsafeProduct() {
      when(productRepo.findById(13L)).thenReturn(of(new Product()));
      facade.getProduct(13L);
   }

   @Test
   public void throwsForInactiveSupplier() {
      Product product = new Product()
          .setExternalRef("exref")
          .setSupplier(new Supplier()
              .setActive(false));
      when(safetyClient.isSafe("exref")).thenReturn(true);
      when(productRepo.findById(13L)).thenReturn(of(product));
      IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
          facade.getProduct(13L));
//      assertTrue(ex.getMessage().contains("supplier"));
      assertThat(ex.getMessage()).containsIgnoringCase("supplier");
   }
   @Test
   public void ok() {
      Product product = new Product()
          .setExternalRef("exref")
          .setName("product-name")
          .setSupplier(new Supplier()
              .setActive(true));

      when(safetyClient.isSafe("exref")).thenReturn(true);
      when(productRepo.findById(13L)).thenReturn(of(product));

      ProductDto dto = facade.getProduct(13L);

      assertEquals("product-name", dto.productName);
//      assertEquals("2020-10-02T16:00:00", dto.sampleDate);
      String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      assertThat(dto.sampleDate).startsWith(todayStr);
//      assertThat(dto.sampleDate).isEqualTo("2020-01-01 07:59:55");
   }

//   @TestConfiguration
//   public static class TestConfig {
//      @Bean
//      @Primary
//      public Clock testClock() {
//         return Clock.fixed(Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
//             .parse("2020-01-01 08:00:00")), ZoneId.systemDefault());
//      }
//   }

}
