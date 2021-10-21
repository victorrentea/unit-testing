package victor.testing.mocks.purity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

   @Test
   void computePrices() {
      Coupon coupon1 = new Coupon(HOME, 2);
      Coupon coupon2 = new Coupon(ELECTRONICS, 4);
      Customer customer = new Customer().setId(13L).setCoupons(List.of(coupon1, coupon2));
      Product p1 = new Product().setId(1L).setCategory(HOME);
      Product p2 = new Product().setId(2L).setCategory(KIDS);
      Map<Long, Double> internalPrices = Map.of(
          1L, 10d,
          2L, 5d);

      PriceComputationResult result = priceService.doComputePrices(customer, List.of(p1, p2), internalPrices);

      assertThat(result.usedCoupons()).containsExactly(coupon1);
      assertThat(result.finalPrices())
          .containsEntry(1L, 8d)
          .containsEntry(2L, 5d);
   }

}