package victor.testing.design.app.repo;

import victor.testing.design.app.domain.Coupon;

import java.util.List;

public interface CouponRepo {
   void markUsedCoupons(long customerId, List<Coupon> usedCoupons);
}
