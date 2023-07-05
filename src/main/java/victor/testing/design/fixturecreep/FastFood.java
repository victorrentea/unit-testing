package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;

@RequiredArgsConstructor
class Shawarma {
  private final Dependency dependency;
  private final FeatureFlags featureFlags;

  public void makeShawarma(boolean takeaway) {
    if (!dependency.isOnionAllowed()) {
      throw new IllegalArgumentException();
    }
     if (takeaway) {
       if (featureFlags.isActive(PORK_SHAWARMA)) {
         // stuff
       }

     }
    // complex logic: 7 ifs
  }
}

@RequiredArgsConstructor
class Tzatziki {
  private final Dependency dependency;
  public void makeTzatziki() {
    if (!dependency.isCucumberAllowed()) {
      throw new IllegalArgumentException();
    }
    // complex logic: 5 ifs
  }
}
