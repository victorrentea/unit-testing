package victor.testing.mocks.purity;

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
   private final ThirdPartyPrices thirdPartyPrices;
   private final CouponRepo couponRepo;
   private final ProductRepo productRepo;
   //@PreAu
   public Map<Long, Double> computePrices(long customerId, List<Long> productIds, Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);

      Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);

      PriceComputationResults results = applyCoupons(products, resolvedPrices, customer.getCoupons());

      // BEWARE OF these in java: keep them under control;
//      reflection ?/ don't use !'
      // SpEL
      // APIs (structure of DTOs
      couponRepo.markUsedCoupons(customerId, results.usedCoupons());
      return results.finalPrices();
   }

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

   private PriceComputationResults applyCoupons(List<Product> products, Map<Long, Double> resolvedPrices, List<Coupon> coupons) {
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
      return new PriceComputationResults(usedCoupons, finalPrices);
   }

   record PriceComputationResults(
           List<Coupon> usedCoupons,
           Map<Long, Double> finalPrices
   ) {

   }

}



//
//f(x:PriceService) {
//   x.thePurePart()
//}