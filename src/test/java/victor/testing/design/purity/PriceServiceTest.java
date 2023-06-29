package victor.testing.design.purity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.design.purity.PriceService.ComputePriceResult;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.repo.ProductRepo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static victor.testing.spring.product.domain.ProductCategory.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

  //   @Test
//   void computePrices() {
//      // given
//      Coupon coupon1 = new Coupon(HOME, 2, Set.of(13L));
//      Coupon coupon2 = new Coupon(ELECTRONICS, 4, Set.of(13L));
//      Customer customer = new Customer().setCoupons(List.of(coupon1, coupon2));
//      when(customerRepo.findById(13L)).thenReturn(customer);
//      Product p1 = new Product().setId(1L).setCategory(HOME).setSupplier(new Supplier().setId(13L));
//      Product p2 = new Product().setId(2L).setCategory(KIDS).setSupplier(new Supplier().setId(13L));
//      when(productRepo.findAllById(List.of(1L, 2L))).thenReturn(List.of(p1, p2));
//      when(thirdPartyPrices.retrievePrice(2L)).thenReturn(5d);
//
//      // when
//      Map<Long, Double> result = priceService.computePrices(13L, List.of(1L, 2L), Map.of(1L, 10d));
//
//      // then
//      verify(couponRepo).markUsedCoupons(eq(13L), couponCaptor.capture());
//      assertThat(couponCaptor.getValue()).containsExactly(coupon1);
//      assertThat(result)
//          .containsEntry(1L, 8d)
//          .containsEntry(2L, 5d);
//   }
  @Test
  void computePricesTstingLogicAsPureFunction() {
    // given
    Coupon coupon1 = new Coupon(HOME, 2, Set.of(13L));
    Coupon coupon2 = new Coupon(ELECTRONICS, 4, Set.of(13L));
    Product p1 = new Product().setId(1L).setCategory(HOME).setSupplier(new Supplier().setId(13L));
    Product p2 = new Product().setId(2L).setCategory(KIDS).setSupplier(new Supplier().setId(13L));

    // when
    ComputePriceResult result = PriceService.doComputePrice(
        List.of(p1, p2),
        Map.of(p1.getId(), 10d, p2.getId(), 5d),
        List.of(coupon1, coupon2));

    // then
    assertThat(result.usedCoupons()).containsExactly(coupon1);
    assertThat(result.finalPrices())
        .containsEntry(1L, 8d)
        .containsEntry(2L, 5d);
  }

}