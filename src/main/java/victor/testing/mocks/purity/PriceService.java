package victor.testing.mocks.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import victor.testing.builder.Coupon;
import victor.testing.builder.Customer;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;

import java.util.*;

@RequiredArgsConstructor
public class PriceService {
   private final CustomerRepo customerRepo;
   private final ThirdPartyPrices thirdPartyPrices;
   private final CouponRepo couponRepo;
   private final ProductRepo productRepo;

   public Map<Long, Double> computePrices(long customerId, List<Long> productIds, Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);

//      PriceResult result = computePrice(customer, products, internalPrices);

      Map<Long, Double> resolvedPrices = resolvePrices(products, internalPrices);

      PriceResult result = computePrices(products, resolvedPrices, customer.getCoupons());

      couponRepo.markUsedCoupons(customerId, result.getUsedCoupons());
      return result.getFinalPrices();
   }

   @VisibleForTesting
   PriceResult computePrices(List<Product> products, Map<Long, Double> resolvedPrices, List<Coupon> coupons) {
      List<Coupon> usedCoupons = new ArrayList<>();
      Map<Long, Double> finalPrices = new HashMap<>();
      for (Product product : products) {
         Double price = resolvedPrices.get(product.getId());
         for (Coupon coupon : coupons) {
            if (coupon.autoApply() && coupon.isApplicableFor(product) && !usedCoupons.contains(coupon)) {
               price = coupon.apply(product, price);
               usedCoupons.add(coupon);
            }
         }
         finalPrices.put(product.getId(), price);
      }
      return new PriceResult(usedCoupons, finalPrices);
   }

   private Map<Long, Double> resolvePrices(List<Product> products, Map<Long, Double> internalPrices) {
      Map<Long, Double> resolvedPrices = new HashMap<>();
      for (Product product : products) { // 1ns
         Double price = internalPrices.get(product.getId());
         if (price == null) {
            price = thirdPartyPrices.retrievePrice(product.getId()); // 5ms - 300ms
         }
         resolvedPrices.put(product.getId(), price);
      }
      return resolvedPrices;
   }

}
class X {
   PriceService s;

   public void method() {
      s.computePrices(Collections.emptyList(), Collections.emptyMap(), Collections.emptyList());
   }
}

@Value
class PriceResult {
   List<Coupon> usedCoupons;
   Map<Long, Double> finalPrices;
}
