package victor.testing.mocks.purity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.builder.Coupon;
import victor.testing.spring.domain.Product;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.domain.ProductCategory.*;

// testNG (not JUNIT) shares the instance for all @Test
@ExtendWith(MockitoExtension.class)
class PriceServiceTest {
   public static final Coupon HOME_2_COUPON = new Coupon(HOME, 2);
   public static final Coupon ELECTRONICS_2_COUPON = new Coupon(ELECTRONICS, 4);
   //   @Mock
//   CustomerRepo customerRepo;
//   @Mock
//   ThirdPartyPrices thirdPartyPrices;
//   @Mock
//   CouponRepo couponRepo;
//   @Mock
//   ProductRepo productRepo;
   @InjectMocks
   PriceService priceService;
//   @Captor
//   ArgumentCaptor<List<Coupon>> couponCaptor;

   private Product product1 = new Product().setId(1L).setCategory(HOME);
   private Product product2 = new Product().setId(2L).setCategory(KIDS);

   @Test
   void sgkldskldskflskldskfldskfld() { // by default junit runs your methods in a 'pseudo-chaotical' order, but based on the method names.
      System.out.println(product1.getId());


   }
   @Test
   void computePrices() {
      List<Coupon> coupons = List.of(HOME_2_COUPON, ELECTRONICS_2_COUPON);
      List<Product> products = List.of(product1, product2);
      Map<Long, Double> prices = Map.of(1L, 10d, 2L, 5d);

      // when
      PriceResult result = priceService.computePrices(products,  prices, coupons);

      assertThat(result.getUsedCoupons()).containsExactly(HOME_2_COUPON);
      assertThat(result.getFinalPrices())
          .containsEntry(1L, 8d)
          .containsEntry(2L, 5d);
   }

}