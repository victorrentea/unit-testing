package victor.testing.mocks.purity;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import victor.testing.builder.Coupon;
import victor.testing.builder.Customer;
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

      PriceResult result = computePrice(customer, products, internalPrices);

      couponRepo.markUsedCoupons(customerId, result.getUsedCoupons());
      return result.getFinalPrices();
   }

   //subcutaneous test
   PriceResult computePrice(Customer customer, List<Product> products, Map<Long, Double> internalPrices) {
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
      PriceResult result = new PriceResult(usedCoupons, finalPrices);
      return result;
   }

}

@Value
class PriceResult {
   List<Coupon> usedCoupons;
   Map<Long, Double> finalPrices;
}
