package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FastFood {
   private final Dependency dependency;
//   private final Features features;

   public void makeShawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException("NEVER!");
      }
//      if (condition ) {
         // STUFF 50%
//         if (features.hasFeature(....)){
//         }
//      }
      // complex logic: 7 ifs 50%
   }

   public void makeTzatziki() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic: 5 ifs
   }
}
