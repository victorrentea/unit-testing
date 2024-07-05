package victor.testing.design.purity;

import java.util.List;
import java.util.Map;

public class ClasaDinProd {
private final PriceService priceService;

  public ClasaDinProd(PriceService priceService) {
    this.priceService = priceService;

  }

  public void method() {
    priceService.applyCoupons(List.of(), Map.of(), null);
  }
}
