package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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

  // i will test this method to explore all corners of the behavior
  // EXCEPT the corners of the pure function
  public Map<Long, Double> computePrices(
      long customerId,
      List<Long> productIds,
      Map<Long, Double> internalPrices) {
    Customer customer = customerRepo.findById(customerId);
    List<Product> products = productRepo.findAllById(productIds); // SELECT * FROM product WHERE id IN (...)

    Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);

    CouponApplicationResult result = applyCoupons(products, resolvedPrices, customer.getCoupons());
    couponRepo.markUsedCoupons(customerId, result.getUsedCoupons());
    return result.getFinalPrices();
  }

  // pure function
  @VisibleForTesting
  CouponApplicationResult applyCoupons(
      List<Product> products,
      Map<Long, Double> resolvedPrices,
      List<Coupon> coupons) {
    List<Coupon> usedCoupons = new ArrayList<>();
    Map<Long, Double> finalPrices = new HashMap<>();
    for (Product product : products) {
      Double price = resolvedPrices.get(product.getId());
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
    return new CouponApplicationResult(usedCoupons, finalPrices);
  }

  @Value
  @VisibleForTesting
  static class CouponApplicationResult {
    List<Coupon> usedCoupons;
    Map<Long, Double> finalPrices;
  }

  private Map<Long, Double> resolvePrices(Map<Long, Double> internalPrices, List<Product> products) {
    Map<Long, Double> resolvedPrices = new HashMap<>();
    for (Product product : products) {
      Double price = internalPrices.get(product.getId());
      if (price == null) {
        price = thirdPartyPricesApi.fetchPrice(product.getId()); // REST GET call
      }
      resolvedPrices.put(product.getId(), price);
    }
    return resolvedPrices;
  }

}

