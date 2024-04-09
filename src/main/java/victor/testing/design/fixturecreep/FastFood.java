package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;

@RequiredArgsConstructor
public class FastFood {
   private final Dependency dependency;
   private final FeatureFlags featureFlags;

   public void makeShawarma(boolean withMeat) {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      if (withMeat) {
         if (featureFlags.isActive(PORK_SHAWARMA)) {
            // stuff
         }
      // complex logic: 7 ifs
      }
   }
// TODO taie aici---------
   public void makeTzatziki() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic: 5 ifs
   }
}
