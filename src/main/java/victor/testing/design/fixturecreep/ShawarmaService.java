package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;

@RequiredArgsConstructor
public class ShawarmaService {
   private final Dependency dependency;
   private final FeatureFlags featureFlags;

   public void makeShawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      if (featureFlags.isActive(PORK_SHAWARMA)) {
         // stuff
      }
      // complex logic: 7 ifs
   }
}
