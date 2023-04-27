package victor.testing.design.purity;

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
         // cate teste aveti nevoie sa puneti pe codul pana aici: 2
         // cate mockuri: 4
         // ----------------------------------
         // 7 teste, cu 0 mockuri
         for (Coupon coupon : customer.getCoupons()) { // 1
            if (coupon.autoApply() // 1
                && coupon.isApplicableFor(product, price) //4
                && !usedCoupons.contains(coupon)) { // 1
               price = coupon.apply(product, price);
               usedCoupons.add(coupon);
            }
         }
         finalPrices.put(product.getId(), price);
      }
      couponRepo.markUsedCoupons(customerId, usedCoupons);
      return finalPrices;
   }

}

