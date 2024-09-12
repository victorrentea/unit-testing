package victor.testing.design.purity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Test {
  private final PriceService priceService;

  public void pr() {
    PriceService.applyCoupons(null, null, null);
  }
}
