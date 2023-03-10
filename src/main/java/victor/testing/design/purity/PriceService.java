package victor.testing.design.purity;

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

   public Map<Long, Double> computePrices(long customerId,
                                          List<Long> productIds,
                                          Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);

      PriceCalculationResult result = doComputePrices(internalPrices, customer, products);

      couponRepo.markUsedCoupons(customerId, result.usedCoupons());
      return result.finalPrices();
   }

   // no SRP
   // not PURE: retrievePrices might give you different resultts 2nd time
   private PriceCalculationResult doComputePrices(Map<Long, Double> internalPrices, Customer customer, List<Product> products) {
      List<Coupon> usedCoupons = new ArrayList<>();
      Map<Long, Double> finalPrices = new HashMap<>();
      for (Product product : products) {
         Double price = internalPrices.get(product.getId());
         if (price == null) {
            price = thirdPartyPrices.retrievePrice(product.getId()); // GET
         }
         for (Coupon coupon : customer.getCoupons()) {
            if (coupon.autoApply() &&
                coupon.isApplicableFor(product) &&
                !usedCoupons.contains(coupon)) {

               price = coupon.apply(product, price);
               usedCoupons.add(coupon);
            }
         }
         finalPrices.put(product.getId(), price);
      }
      PriceCalculationResult result = new PriceCalculationResult(finalPrices, usedCoupons);
      return result;
   }

}
record PriceCalculationResult(Map<Long, Double> finalPrices,
                              List<Coupon> usedCoupons) {}
