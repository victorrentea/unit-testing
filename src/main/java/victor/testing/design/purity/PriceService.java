package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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

   public Map<Long, Double> computePrices(long customerId,
                                          List<Long> productIds,
                                          Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);
      Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);
      PriceCalculationResult result = doComputePrices(products, resolvedPrices, customer.getCoupons());
      couponRepo.markUsedCoupons(customerId, result.getUsedCoupons());
      return result.getFinalPrices();
   }

   @VisibleForTesting
    static PriceCalculationResult doComputePrices(
       List<Product> products, Map<Long, Double> resolvedPrices, List<Coupon> coupons) {
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
      return new PriceCalculationResult(usedCoupons, finalPrices);
   }

   @Value
   static class PriceCalculationResult{ // downside, pret
      List<Coupon> usedCoupons ;
      Map<Long, Double> finalPrices;
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
   //3 @Test x 4 @Mock = OK
   //9 @Test x 4 @Mock =  NU MAI E OK

}

