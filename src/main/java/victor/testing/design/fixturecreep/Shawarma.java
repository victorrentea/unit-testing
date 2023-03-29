package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Shawarma {
   private final Dependency dependency;

   public void makeShawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic
   }

   public void makeTzatziki() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic
   }
}
