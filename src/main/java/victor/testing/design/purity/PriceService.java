package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.testing.design.app.domain.Coupon;
import victor.testing.design.app.domain.Customer;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.infra.ThirdPartyPricesApi;
import victor.testing.design.app.repo.CouponRepo;
import victor.testing.design.app.repo.CustomerRepo;
import victor.testing.design.app.repo.ProductRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PriceService {
   private final CustomerRepo customerRepo;
   private final ThirdPartyPricesApi thirdPartyPricesApi;
   private final CouponRepo couponRepo;
   private final ProductRepo productRepo;

   public Map<Long, Double> computePrices(long customerId, List<Long> productIds,
                                          Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);
      Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);
      PriceCalculationResult result = doComputePrices(products, resolvedPrices, customer.getCoupons());
      couponRepo.markUsedCoupons(customerId, result.usedCoupons());
      return result.finalPrices();
   }

   public record PriceCalculationResult(Map<Long, Double> finalPrices, List<Coupon> usedCoupons) {
   }

   // static means: no NETWORKING
   // PURE
   @VisibleForTesting
   static PriceCalculationResult doComputePrices(List<Product> products,
                                                 Map<Long, Double> resolvedPrices,
                                                 List<Coupon> coupons) {
      Map<Long, Double> finalPrices = new HashMap<>();
      List<Coupon> usedCoupons = new ArrayList<>();
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
      return new PriceCalculationResult(finalPrices, usedCoupons);
   }

   private Map<Long, Double> resolvePrices(Map<Long, Double> internalPrices, List<Product> products) {
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

