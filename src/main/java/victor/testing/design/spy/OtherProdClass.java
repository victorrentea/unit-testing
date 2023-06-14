package victor.testing.design.spy;

import lombok.RequiredArgsConstructor;
import victor.testing.design.spy.God;

@RequiredArgsConstructor
public class OtherProdClass {
  private final God god;

  public void method() {
    god.low(null);
  }
}
