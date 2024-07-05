package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;

@RequiredArgsConstructor
public class FastFood {
   private final Dependency dependency;
   private final FeatureFlags featureFlags;

   public void makeShawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      if (featureFlags.isActive(PORK_SHAWARMA)) {
         // 🐽
      }
      // complex logic: 7 ifs
   }

   public void makeTzatziki() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic: 5 ifs
   }
}
