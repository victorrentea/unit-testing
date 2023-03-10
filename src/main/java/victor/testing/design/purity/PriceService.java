package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
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
      Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);

      PriceCalculationResult result = doComputePricesPure(customer, products, resolvedPrices);

      couponRepo.markUsedCoupons(customerId, result.usedCoupons());
      return result.finalPrices();
   }

   @NotNull
   private Map<Long, Double> resolvePrices(Map<Long, Double> internalPrices, List<Product> products) {
      Map<Long, Double> resolvedPrices = new HashMap<>();
      for (Product product : products) {
         Double price = internalPrices.get(product.getId());
         if (price == null) {
            price = thirdPartyPrices.retrievePrice(product.getId()); // GET
         }
         resolvedPrices.put(product.getId(), price);
      }
      return resolvedPrices;
   }

   // static method + immutable object = pure function
   @VisibleForTesting
//   @Contract(pure = true)
    static PriceCalculationResult doComputePricesPure(Customer customer, List<Product> products, Map<Long, Double> resolvedPrices) {
      List<Coupon> usedCoupons = new ArrayList<>();
      Map<Long, Double> finalPrices = new HashMap<>();
      for (Product product : products) {
         double price = resolvedPrices.get(product.getId());
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
