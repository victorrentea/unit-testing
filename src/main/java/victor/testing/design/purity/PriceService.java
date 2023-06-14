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
      List<Product> products = productRepo.findAllById(productIds);
      Map<Long, Double> initialPrices = resolvePrices(internalPrices, products);
      PriceCalculationResult result = applyCoupons(products, initialPrices, customer.getCoupons());
      couponRepo.markUsedCoupons(customerId, result.usedCoupons());
      return result.finalPrices();
   }

   @NotNull
   private Map<Long, Double> resolvePrices(Map<Long, Double> internalPrices, List<Product> products) {
      Map<Long, Double> initialPrices = new HashMap<>();
      for (Product product : products) {
         Double price = internalPrices.get(product.getId());
         if (price == null) {
            price = thirdPartyPrices.retrievePrice(product.getId());
         }
         initialPrices.put(product.getId(), price);
      }
      return initialPrices;
   }

   public record PriceCalculationResult(List<Coupon> usedCoupons, Map<Long, Double> finalPrices) {
   }

   // PURE FUNCTION: no side effects (no POST, INSERT, no field changed in any object) and same results for same inputs
   // static functions cannot use the Spring injected dependecies
   @VisibleForTesting
   // i openened the real complexity of this class for easier testing
   // with a "Subcutaneous Test" avoiding the annoying dependencies (@Mock)
    static PriceCalculationResult applyCoupons(List<Product> products,
                                                      Map<Long, Double> initialPrices,
                                                      List<Coupon> coupons) {
      List<Coupon> usedCoupons = new ArrayList<>();
      Map<Long, Double> finalPrices = new HashMap<>();
      for (Product product : products) {
         Double price = initialPrices.get(product.getId());
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

}

