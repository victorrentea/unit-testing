package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FastFood {
   private final Dependency dependency;

   public void makeShawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
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
