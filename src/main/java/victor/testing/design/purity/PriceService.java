package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PriceService {
  private final CustomerRepo customerRepo;
  private final ThirdPartyPrices thirdPartyPrices;
  private final CouponRepo couponRepo;
  private final ProductRepo productRepo;

    // 2 tests x 4 mocks = tolerate
    // 8 tests x 4 mocks = NO-NO!!
  public Map<Long, Double> computePrices(long customerId,
                                         List<Long> productIds,
                                         Map<Long, Double> internalPrices) {
    Customer customer = customerRepo.findById(customerId);
    List<Product> products = productRepo.findAllById(productIds); // SELECT * FROM PRODUCT WHERE ID IN(?,?,?...)
    Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);
    ApplyCouponsResult result = applyCoupons(products, resolvedPrices, customer.getCoupons());
    couponRepo.markUsedCoupons(customerId, result.usedCoupons());
    return result.finalPrices();
  }

  record ApplyCouponsResult(List<Coupon> usedCoupons, Map<Long, Double> finalPrices) {
  }

  // subcutaneous tests
  // a static function that does not change state of its params (and not uses time/random) IS PURE
  @VisibleForTesting // PURE FUNCTION HOSTING most of my complexity : opened if for testing
  static ApplyCouponsResult applyCoupons(List<Product> products, Map<Long, Double> resolvedPrices, List<Coupon> coupons) {
    List<Coupon> usedCoupons = new ArrayList<>();
    Map<Long, Double> finalPrices = new HashMap<>();
    for (Product product : products) {
      Double price = resolvedPrices.get(product.getId());
      for (Coupon coupon : coupons) {      // 6 tests
        if (coupon.autoApply()
            && coupon.isApplicableFor(product, price)
            && !usedCoupons.contains(coupon)) {
          price = coupon.apply(product, price);
          usedCoupons.add(coupon);
        }
      }
      finalPrices.put(product.getId(), price);
    }
    return new ApplyCouponsResult(usedCoupons, finalPrices);
  }

  @NotNull
  private Map<Long, Double> resolvePrices(Map<Long, Double> internalPrices, List<Product> products) {
    Map<Long, Double> resolvedPrices = new HashMap<>();
    for (Product product : products) {
      Double price = internalPrices.get(product.getId());
      if (price == null) {
        price = thirdPartyPrices.retrievePrice(product.getId());
      }
      resolvedPrices.put(product.getId(), price);
    }
    return resolvedPrices;
  }

}

