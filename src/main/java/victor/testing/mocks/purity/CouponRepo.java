package victor.testing.mocks.purity;

import victor.testing.builder.Coupon;

import java.util.List;

public interface CouponRepo {
   void markUsedCoupons(long customerId, List<Coupon> usedCoupons);
}
