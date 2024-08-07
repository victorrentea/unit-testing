package victor.testing.design.purity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static victor.testing.spring.entity.ProductCategory.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {
   @Mock
   CustomerRepo customerRepo;
   @Mock
   ThirdPartyPricesApi thirdPartyPricesApi;
   @Mock
   CouponRepo couponRepo;
   @Mock
   ProductRepo productRepo;
   @InjectMocks
   PriceService priceService;
   @Captor
   ArgumentCaptor<List<Coupon>> couponCaptor;

   @Test
   void computePrices() {
      Coupon coupon1 = new Coupon(HOME, 2, Set.of(13L));
      Coupon coupon2 = new Coupon(ELECTRONICS, 4, Set.of(13L));
      Customer customer = new Customer().setCoupons(List.of(coupon1, coupon2));
      when(customerRepo.findById(13L)).thenReturn(customer);
      Product p1 = new Product().setId(1L).setCategory(HOME).setSupplier(new Supplier().setId(13L));
      Product p2 = new Product().setId(2L).setCategory(KIDS).setSupplier(new Supplier().setId(13L));
      when(productRepo.findAllById(List.of(1L, 2L))).thenReturn(List.of(p1, p2));
      when(thirdPartyPricesApi.fetchPrice(2L)).thenReturn(5d);

      Map<Long, Double> result = priceService.computePrices(13L, List.of(1L, 2L), Map.of(1L, 10d));

      verify(couponRepo).markUsedCoupons(eq(13L), couponCaptor.capture());
      assertThat(couponCaptor.getValue()).containsExactly(coupon1);

      assertThat(result)
          .containsEntry(1L, 8d)
          .containsEntry(2L, 5d);
   }

}