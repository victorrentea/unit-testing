package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
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
  private final ThirdPartyPricesApi thirdPartyPricesApi;
  private final CouponRepo couponRepo;
  private final ProductRepo productRepo;

  public Map<Long, Double> computePrices(
      long customerId,
      List<Long> productIds,
      Map<Long, Double> internalPrices) {
    Customer customer = customerRepo.findById(customerId);
    List<Product> products = productRepo.findAllById(productIds);

    Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);

    DiscountResult result = applyCoupons(products, resolvedPrices, customer.getCoupons());
    couponRepo.markUsedCoupons(customerId, result.usedCoupons());
    return result.finalPrices();
  }

// records were added to Java to discourage the use of Tuples
record DiscountResult(List<Coupon> usedCoupons, Map<Long, Double> finalPrices) {
  }

  // a static method cannot use any @Injected deps to go out on the network + [c]DI
  // 'static' is bad because it's not OOP. instead of a static f(Customer) --> customer.f() = OOP
  @VisibleForTesting // fail Sonar/IJ for if accessed from src/main
  // "subcutaneous tests" avoiding the heavy dependencies and targetting the "MEAT" (complexity) of the problem
  static DiscountResult applyCoupons(List<Product> products, Map<Long, Double> initialPrices, List<Coupon> coupons) {
    List<Coupon> usedCoupons = new ArrayList<>();
    Map<Long, Double> finalPrices = new HashMap<>();
    for (Product product : products) {
      Double price = initialPrices.get(product.getId());
      // high complexity
      for (Coupon coupon : coupons) {
        if (coupon.autoApply()
            && coupon.isApplicableFor(product, price)
            && !usedCoupons.contains(coupon)) {
          price = coupon.apply(product, price);
          usedCoupons.add(coupon);
        }
      }

      finalPrices.put(product.getId(), price);
    }
    return new DiscountResult(usedCoupons, finalPrices);
  }

  private Map<Long, Double> resolvePrices(Map<Long, Double> internalPrices, List<Product> products) {
    Map<Long, Double> resolvedPrices = new HashMap<>();// YES
    for (Product product : products) {
      // resolve prices
      Double price = internalPrices.get(product.getId());
      if (price == null) {
        price = thirdPartyPricesApi.fetchPrice(product.getId());
      }
      resolvedPrices.put(product.getId(), price);
    }
    return resolvedPrices;
  }

}

