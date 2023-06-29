package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.repo.ProductRepo;

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

   public Map<Long, Double> computePrices(long customerId, List<Long> productIds,
                                          Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds); // where id IN (?,?..)
      Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);
      ComputePriceResult result = doComputePrice(products, resolvedPrices, customer.getCoupons());
      couponRepo.markUsedCoupons(customerId, result.usedCoupons());
      return result.finalPrices();
   }

   // heavy decisions
   record ComputePriceResult(List<Coupon> usedCoupons, Map<Long, Double> finalPrices) {
   }

   // pure functions should be static
   // if a static function does NOT mutate args => pure (if not time/random contact)
   // static means no dependecy contact => no network
   @VisibleForTesting // this is a GOOD usage of it
   static ComputePriceResult doComputePrice(List<Product> products, Map<Long, Double> resolvedPrices, List<Coupon> coupons) {
      List<Coupon> usedCoupons = new ArrayList<>();
      Map<Long, Double> finalPrices = new HashMap<>();
      for (Product product : products) {
         double price = resolvedPrices.get(product.getId());
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
      return new ComputePriceResult(usedCoupons, finalPrices);
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

