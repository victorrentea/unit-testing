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

   public Map<Long, Double> computePrices(long customerId, List<Long> productIds, Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);
      List<Coupon> usedCoupons = new ArrayList<>();
      Map<Long, Double> finalPrices = new HashMap<>();
      for (Product product : products) {
         Double price = internalPrices.get(product.getId());
         if (price == null) {
            price = thirdPartyPrices.retrievePrice(product.getId());
         }
         for (Coupon coupon : customer.getCoupons()) {
            price = F(usedCoupons, product, price, coupon);
         }
         finalPrices.put(product.getId(), price);
      }
      couponRepo.markUsedCoupons(customerId, usedCoupons);
      return finalPrices;
   }

   @VisibleForTesting
    static Double F(List<Coupon> usedCoupons, Product product, Double price, Coupon coupon) {
      if (coupon.autoApply()
          && coupon.isApplicableFor(product, price)
          && !usedCoupons.contains(coupon)) {
         price = coupon.apply(product, price);
         usedCoupons.add(coupon);
      }
      return price;
   }

}

