package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
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

   public Map<Long, Double> computePrices(long customerId, List<Long> productIds, Map<Long, Double> internalPrices) {
      Customer customer = customerRepo.findById(customerId);
      List<Product> products = productRepo.findAllById(productIds);
      Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);

      PriceCalculationResult result = computePrices(products, resolvedPrices, customer.getCoupons());

      couponRepo.markUsedCoupons(customerId, result.usedCoupons());
      return result.finalPrices();
   }

   @NotNull
   private Map<Long, Double> resolvePrices(Map<Long, Double> internalPrices, List<Product> products) {
      Map<Long, Double> resolvedPrices = new HashMap<>();
      for (Product product : products) {
         Double price = internalPrices.get(product.getId());
         if (price == null) {
            price = thirdPartyPrices.retrievePrice(product.getId());
         }
         // cate teste aveti nevoie sa puneti pe codul pana aici: 2
         // cate mockuri: 4
         resolvedPrices.put(product.getId(), price);
      }
      return resolvedPrices;
   }
   // o metoda statica (=nu face retea, repo, api) care nu modifica parametrii primiti este pura

   @NotNull
   @VisibleForTesting // teste subcutanate direct catre logica.
   PriceCalculationResult computePrices(List<Product> products,
                                                       Map<Long, Double> resolvedPrices,
                                                       List<Coupon> coupons) {
      List<Coupon> usedCoupons = new ArrayList<>();
      Map<Long, Double> finalPrices = new HashMap<>();
      for (Product product : products) {
         double price = resolvedPrices.get(product.getId());
         // ----------------------------------
         // 7 teste, cu 0 mockuri
         for (Coupon coupon : coupons) { // 1
            if (coupon.autoApply() // 1
                && coupon.isApplicableFor(product, price) //4
                && !usedCoupons.contains(coupon)) { // 1
               price = coupon.apply(product, price);
               usedCoupons.add(coupon);
            }
         }
         finalPrices.put(product.getId(), price);
      }
      return new PriceCalculationResult(usedCoupons, finalPrices);
   }
   // @Value
   record PriceCalculationResult(List<Coupon> usedCoupons, Map<Long, Double> finalPrices) {
   }

}

