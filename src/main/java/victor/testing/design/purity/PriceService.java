package victor.testing.design.purity;

import com.google.common.annotations.VisibleForTesting;
import lombok.NonNull;
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
  private final ThirdPartyPricesApi thirdPartyPricesApi;
  private final CouponRepo couponRepo;
  private final ProductRepo productRepo;

  public Map<Long, Double> computePrices(
      long customerId,
      List<Long> productIds,
      Map<Long, Double> internalPrices) {

    Customer customer = customerRepo.findById(customerId);
    List<Product> products = productRepo.findAllById(productIds);
    Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);
    CouponDisscountResult result = applyCoupons(products, resolvedPrices, customer);
    couponRepo.markUsedCoupons(customerId, result.usedCoupons());
    return result.finalPrices();
  }

  @VisibleForTesting
  record CouponDisscountResult(List<Coupon> usedCoupons, Map<Long, Double> finalPrices) {
  }

  // teste subcutanate (injectezi testul sub pielea cu dependinte)
  // dar dogma zice sa testezi privatele prin publice. dar aici trisam.
  // mai ortodox ar fi sa extragem metoda in alt obiect si sa testam acel obiect
  //   insa unii ar putea spune ca asta devine "TEST-INDUCED DESIGN DAMAGE" ca iti fragmentezi
  //   codul excesiv doar pt teste.
  @VisibleForTesting
  CouponDisscountResult applyCoupons(List<Product> products, Map<Long, Double> resolvedPrices, Customer customer) {
    List<Coupon> usedCoupons = new ArrayList<>();
    Map<Long, Double> finalPrices = new HashMap<>();
    for (Product product : products) {
      Double price = resolvedPrices.get(product.getId());
      for (Coupon coupon : customer.getCoupons()) {
        if (coupon.autoApply()
            && coupon.isApplicableFor(product, price)
            && !usedCoupons.contains(coupon)) {
          price = coupon.apply(product, price);
          usedCoupons.add(coupon);
        }
      }
      finalPrices.put(product.getId(), price);
    }
    return new CouponDisscountResult(usedCoupons, finalPrices);
  }

  private @NonNull Map<Long, Double> resolvePrices(Map<Long, Double> internalPrices, List<Product> products) {
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

