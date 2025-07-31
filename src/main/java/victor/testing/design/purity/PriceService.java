package victor.testing.design.purity;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import victor.testing.mutation.Customer;
import victor.testing.spring.entity.Product;
import victor.testing.spring.repo.ProductRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PriceService {
  private final CustomerRepo customerRepo;
  private final ThirdPartyPricesApi thirdPartyPricesApi;
  private final CouponRepo couponRepo;
  private final ProductRepo productRepo;
  private final AltaClasaInProd altaClasaInProd;

  public Map<Long, Double> computePrices(
      long customerId,
      List<Long> productIds,
      Map<Long, Double> internalPrices) {
    Customer customer = customerRepo.findById(customerId);
    List<Product> products = productRepo.findAllById(productIds); // SELECT WHERE ID IN  (?,?..)

    Map<Long, Double> resolvedPrices = resolvePrices(internalPrices, products);

    // apply coupons
    AltaClasaInProd.ApplyCouponResult result = altaClasaInProd.applyCoupons(products, resolvedPrices, customer);

    couponRepo.markUsedCoupons(customerId, result.usedCoupons());
    return result.finalPrices();
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

