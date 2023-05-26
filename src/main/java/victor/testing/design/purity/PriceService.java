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

    // apply coupons
    ApplyCouponsResult result = applyCoupons(customer, products, resolvedPrices);

    couponRepo.markUsedCoupons(customerId, result.usedCoupons());
    return result.finalPrices();
  }

  record ApplyCouponsResult(List<Coupon> usedCoupons, Map<Long, Double> finalPrices) {
  }

  @VisibleForTesting
  static ApplyCouponsResult applyCoupons(Customer customer, List<Product> products, Map<Long, Double> resolvedPrices) {
    List<Coupon> usedCoupons = new ArrayList<>();
    Map<Long, Double> finalPrices = new HashMap<>();
    for (Product product : products) {
      Double price = resolvedPrices.get(product.getId());
      for (Coupon coupon : customer.getCoupons()) {      // 6 tests
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

