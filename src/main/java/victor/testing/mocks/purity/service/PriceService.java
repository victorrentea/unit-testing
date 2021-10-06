package victor.testing.mocks.purity.service;

import lombok.RequiredArgsConstructor;
import victor.testing.mocks.purity.domain.Coupon;
import victor.testing.mocks.purity.domain.Customer;
import victor.testing.mocks.purity.domain.Product;
import victor.testing.mocks.purity.repo.CouponRepo;
import victor.testing.mocks.purity.repo.CustomerRepo;
import victor.testing.mocks.purity.repo.ProductRepo;

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

   // TODO Homework remove the need of mocks by subcutenous test
   // move the dep calls (queries & commands) to another local method leaving the current one package protected opened for testing.
   // Refactor test. victorrentea@gmail.com
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
            if (coupon.autoApply() && coupon.isApplicableFor(product) && !usedCoupons.contains(coupon)) {
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

