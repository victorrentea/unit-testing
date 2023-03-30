package victor.testing.design.purity;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.VisibleForTesting;
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

   // 2 Cyclomatic complexity
   public Map<Long, Double> computePrices(long customerId, List<Long> productIds, Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);

      Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);

      PriceComputationResult result = computePricesPure(customer, products, resolvedPrices);

      couponRepo.markUsedCoupons(customerId, result.usedCoupons());
      return result.finalPrices();
   }

   // #2 pass an adapter wrapping over the internalPrices MAP - behavior -> mock
   //      PriceResolver priceResolver <--as param

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

   // 10 Cyclomatic complexity
   @VisibleForTesting
   PriceComputationResult computePricesPure(Customer customer, List<Product> products, Map<Long, Double> resolvedPrices) {
      List<Coupon> usedCoupons = new ArrayList<>();
      Map<Long, Double> finalPrices = new HashMap<>();
      for (Product product : products) {
         Double price = resolvedPrices.get(product.getId());
         for (Coupon coupon : customer.getCoupons()) {
            if (coupon.autoApply() && coupon.isApplicableFor(product, price) && !usedCoupons.contains(coupon)) {
               price = coupon.apply(product, price);
               usedCoupons.add(coupon);
            }
         }
         finalPrices.put(product.getId(), price);
      }
      return new PriceComputationResult(finalPrices, usedCoupons);
   }
}

record PriceComputationResult(Map<Long, Double> finalPrices,
                              List<Coupon> usedCoupons) {
}