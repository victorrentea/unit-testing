package victor.testing.design.purity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.spring.entity.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AltaClasaInProd {
  
  public record ApplyCouponResult(List<Coupon> usedCoupons, Map<Long, Double> finalPrices) {}
  
  public ApplyCouponResult applyCoupons(List<Product> products, Map<Long, Double> resolvedPrices, Customer customer) {
    List<Coupon> usedCoupons = new ArrayList<>();
    Map<Long, Double> finalPrices = new HashMap<>();
    for (Product product : products) {
      Double price = resolvedPrices.get(product.getId());
      for (Coupon coupon : customer.getCoupons()) {
        if (coupon.isAutoApply()
            && coupon.isApplicableFor(product, price)
            && !usedCoupons.contains(coupon)) {
          price = coupon.apply(product, price);
          usedCoupons.add(coupon);
        }
      }
      finalPrices.put(product.getId(), price);
    }
    return new ApplyCouponResult(usedCoupons, finalPrices);
  }
}
