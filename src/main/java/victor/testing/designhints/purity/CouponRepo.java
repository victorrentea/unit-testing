package victor.testing.designhints.purity;

import victor.testing.mutation.Coupon;

import java.util.List;

public interface CouponRepo {
   void markUsedCoupons(long customerId, List<Coupon> usedCoupons);
}
