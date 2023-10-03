package victor.testing.design.fixturecreep;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TzatzikiService {// THE ProductService
  private final Dependency dependency;

  public void makeTzatziki() {
    if (!dependency.isCucumberAllowed()) {
      throw new IllegalArgumentException();
    }
    // complex logic: 5 ifs
  }
}
