package victor.testing.design.purity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.design.app.domain.Coupon;
import victor.testing.design.app.domain.Customer;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.domain.Supplier;
import victor.testing.design.app.infra.ThirdPartyPricesApi;
import victor.testing.design.app.repo.CouponRepo;
import victor.testing.design.app.repo.CustomerRepo;
import victor.testing.design.app.repo.ProductRepo;
import victor.testing.design.purity.PriceService.PriceCalculationResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static victor.testing.design.app.domain.ProductCategory.*;

class PriceServiceTest {

   @Test
   void computePrices() {
      Coupon coupon1 = new Coupon(HOME, 2, Set.of(13L));
      Coupon coupon2 = new Coupon(ELECTRONICS, 4, Set.of(13L));
      Product p1 = new Product().setId(1L).setCategory(HOME).setSupplier(new Supplier().setId(13L));
      Product p2 = new Product().setId(2L).setCategory(KIDS).setSupplier(new Supplier().setId(13L));

      PriceCalculationResult result = PriceService.doComputePrices(List.of(p1, p2),
          Map.of(1L, 10d, 2L, 5d), List.of(coupon1, coupon2));

      assertThat(result.usedCoupons()).containsExactly(coupon1);
      assertThat(result.finalPrices())
          .containsEntry(1L, 8d)
          .containsEntry(2L, 5d);
   }

}