package victor.testing.mocks.purity.repo;

import victor.testing.mocks.purity.domain.Coupon;

import java.util.List;

public interface CouponRepo {
   void markUsedCoupons(long customerId, List<Coupon> usedCoupons);
}
