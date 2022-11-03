package victor.testing.design.purity;

import lombok.RequiredArgsConstructor;
import org.testcontainers.shaded.com.google.common.annotations.VisibleForTesting;
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

   public Map<Long, Double> computePrices(long customerId, List<Long> productIds, Map<Long, Double> internalPrices) {
      // READ part (fetchin of data)
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);

      // phase1 = resolve all base pricese
      Map<Long, Double> basePrices = resolveBasePrices(internalPrices, products);

      // phase 2 -- compute => PURE FUNCTION: no network, no changes, same result
      PriceComputationResult result = pureLogic(products, basePrices, customer.getCoupons());

      // side effecting/return (spread the changes)
//      auditService.auditOrder()
      couponRepo.markUsedCoupons(customerId, result.usedCoupons);
      return result.finalPrices;
   }

   private Map<Long, Double> resolveBasePrices(Map<Long, Double> internalPrices, List<Product> products) {
      Map<Long, Double> basePrices = new HashMap<>();
      for (Product product : products) {
         Double price = internalPrices.get(product.getId());
         if (price == null) {
            price = thirdPartyPrices.retrievePrice(product.getId()); // NETWORK OMG
            // INPURE CALL (Memory of a "PURE FUNCTION")
         }
         basePrices.put(product.getId(), price);
      }
      return basePrices;
   }

   // Extra sugar: the most primitive and simple signature you can
   // the ideal: all the most heavy computation we have should be impl as PURE FUNCTIONS
   @VisibleForTesting
   PriceComputationResult pureLogic(List<Product> products,
                                    // the price: more arguments
                                    Map<Long, Double> basePrices,
                                    List<Coupon> customerCoupons) {
      Map<Long, Double> finalPrices = new HashMap<>();
      List<Coupon> usedCoupons = new ArrayList<>();
      for (Product product : products) {
         Double price = basePrices.get(product.getId());
         for (Coupon coupon : customerCoupons) {
            if (coupon.autoApply() &&
                coupon.isApplicableFor(product) &&
                !usedCoupons.contains(coupon)) {

               price = coupon.apply(product, price);
               usedCoupons.add(coupon); // instead of custmer.coupons.remove
            }
         }
         finalPrices.put(product.getId(), price);
      }
      return new PriceComputationResult(finalPrices, usedCoupons);
   }

   record PriceComputationResult(Map<Long, Double> finalPrices, List<Coupon> usedCoupons) {}

   // "Split Phase" -> Introduce Result Object refactor

}

