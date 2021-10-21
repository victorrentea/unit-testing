package victor.testing.mocks.purity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.builder.Coupon;
import victor.testing.builder.Customer;
import victor.testing.mocks.purity.PriceService.PriceComputationResult;
import victor.testing.spring.domain.Product;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.domain.ProductCategory.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {
   @InjectMocks
   PriceService priceService;
   private Product homeProduct = new Product().setId(1L).setCategory(HOME);
   private Product kidsProduct = new Product().setId(2L).setCategory(KIDS);
   private Coupon home2coupon = new Coupon(HOME, 2);
   private Coupon electronics4coupon = new Coupon(ELECTRONICS, 4);
   private Customer customer = new Customer().setId(13L);

   @Test
   void computePrices() {
      customer.setCoupons(List.of(home2coupon, electronics4coupon));

      // NICIODATA
//      PriceComputationResult r = mock(PriceComputationResult.class);
//      when(r.finalPrices()).thenReturn(Map.of());
//
//      r = new PriceComputationResult(Map.of());

      // niciodata nu faci mock de obiect cu date decat daca ai
      // Aggregate + Domain Events heavily used
//      Customer customerMock_anathemaInAfaraDeDDD = Mockito.mock(Customer.class);


      Map<Long, Double> internalPrices = Map.of(
          homeProduct.getId(), 10d,
          kidsProduct.getId(), 5d);
      List<Product> products = List.of(homeProduct, kidsProduct);

      PriceComputationResult result = priceService.doComputePrices(customer, products, internalPrices);

      assertThat(result.usedCoupons()).containsExactly(home2coupon);
      assertThat(result.finalPrices())
          .containsEntry(homeProduct.getId(), 8d)
          .containsEntry(kidsProduct.getId(), 5d);
   }

}