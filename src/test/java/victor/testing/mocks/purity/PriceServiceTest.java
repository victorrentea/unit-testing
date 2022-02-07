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
import victor.testing.spring.domain.Product;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static victor.testing.spring.domain.ProductCategory.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {
//   @Mock
//   CustomerRepo customerRepo;
   @Mock
   ThirdPartyPrices thirdPartyPrices;
//   @Mock
//   CouponRepo couponRepo;
//   @Mock
//   ProductRepo productRepo;
   @InjectMocks
   PriceService priceService;
   @Captor
   ArgumentCaptor<List<Coupon>> couponCaptor;

   @Test
   void computePrices() {
      Coupon coupon1 = new Coupon(HOME, 2);
      Coupon coupon2 = new Coupon(ELECTRONICS, 4);
      Customer customer = new Customer().setCoupons(List.of(coupon1, coupon2));
      Product p1 = new Product().setId(1L).setCategory(HOME);
      Product p2 = new Product().setId(2L).setCategory(KIDS);
      List<Product> products = List.of(p1, p2);
      when(thirdPartyPrices.retrievePrice(2L)).thenReturn(5d);

      // when
      PriceResult result = priceService.computePrice(customer, products, Map.of(1L, 10d));

      assertThat(result.getUsedCoupons()).containsExactly(coupon1);
      assertThat(result.getFinalPrices())
          .containsEntry(1L, 8d)
          .containsEntry(2L, 5d);
   }

}