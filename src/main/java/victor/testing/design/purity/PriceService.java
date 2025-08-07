package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.spring.entity.Product;
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

  // 3-4 @Test x 4 @Mock = :/
  // 12 @Test x 4 @Mock = HATE DE COLEGI🤯

  // neplacut de testat, cere mockuri
  public Map<Long, Double> computePrices(
      long customerId,
      List<Long> productIds,
      Map<Long, Double> internalPrices) {

    Customer customer = customerRepo.findById(customerId);
    List<Product> products = productRepo.findAllById(productIds);

    Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);

    CouponsApplicationResult result = applyCoupons(products, resolvedPrices, customer.getCoupons());
    couponRepo.markUsedCoupons(customerId, result.getUsedCoupons());

    return result.getFinalPrices();
  }

  @Value
  private static final class CouponsApplicationResult {
    List<Coupon> usedCoupons;
    Map<Long, Double> finalPrices;
  }


  @VisibleForTesting // Sonar cries if used from src/main
  // ~ code smell = partially breaks encapsulation of this class for testing purposes
  // you can alwasy move it to another class as a public method.
  static CouponsApplicationResult applyCoupons(
      List<Product> products,
      Map<Long, Double> resolvedPrices,
      List<Coupon> coupons) {
    List<Coupon> usedCoupons = new ArrayList<>();
    Map<Long, Double> finalPrices = new HashMap<>();
    for (Product product : products) {
      Double price = resolvedPrices.get(product.getId());
      for (Coupon coupon : coupons) {
        if (coupon.isAutoApply()
            && coupon.isApplicableFor(product, price)
            && !usedCoupons.contains(coupon)) {
          price = coupon.apply(product, price);
          usedCoupons.add(coupon);
        }
      }
      finalPrices.put(product.getId(), price);
    }
    return new CouponsApplicationResult(usedCoupons, finalPrices);
  }


  private @NonNull Map<Long, Double> resolvePrices(Map<Long, Double> internalPrices, List<Product> products) {
    Map<Long, Double> resolvedPrices = new HashMap<>();
    for (Product product : products) {
      Double price = internalPrices.get(product.getId());
      if (price == null) {
        price = thirdPartyPricesApi.fetchPrice(product.getId());
      }
      resolvedPrices.put(product.getId(), price);
    }
    return resolvedPrices;
  }
}

