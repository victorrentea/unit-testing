package victor.testing.mocks.purity;

import lombok.RequiredArgsConstructor;
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

   public Map<Long, Double> computePrices(long customerId,
                                          List<Long> productIds,
                                          Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);
      Map<Long, Double> prices = resolvePrices(products, internalPrices);
      return doComputePrices(customer, products, prices);
   }

   Map<Long, Double> doComputePrices(Customer customer,
                                             List<Product> products,
                                             Map<Long, Double> prices) {
      List<Coupon> usedCoupons = new ArrayList<>();
      Map<Long, Double> finalPrices = new HashMap<>();
      for (Product product : products) {
         Double price = prices.get(product.getId());

         for (Coupon coupon : customer.getCoupons()) {
            if (coupon.autoApply() && coupon.isApplicableFor(product) && !usedCoupons.contains(coupon)) {
               price = coupon.apply(product, price);
               usedCoupons.add(coupon);
            }
         }
         finalPrices.put(product.getId(), price);
      }
      couponRepo.markUsedCoupons(customer.getId(), usedCoupons);
      return finalPrices;
   }

   private Map<Long, Double> resolvePrices(List<Product> products, Map<Long, Double> internalPrices) {
      Map<Long, Double> prices = new HashMap<>(internalPrices);
      for (Product product : products) {
         Double price = internalPrices.get(product.getId());
         if (price == null) {
            price = thirdPartyPrices.retrievePrice(product.getId());
         }
         prices.put(product.getId(), price);
//         prices.computeIfAbsent(product.getId(), id -> internalPrices.get(id));
      }
      return prices;
   }

}

