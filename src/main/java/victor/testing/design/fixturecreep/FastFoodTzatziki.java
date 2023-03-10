package victor.testing.design.fixturecreep;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FastFoodTzatziki {
  private final Dependency dependency;

  public void makeTzatziki() {
    if (!dependency.isCucumberAllowed()) {
      throw new IllegalArgumentException();
    }
    // complex logic
    // complex logic
    // complex logic
    // complex logic
  }
}
