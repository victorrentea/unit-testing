package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FastFoodShawarma {
   private final Dependency dependency;

   public void makeShawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException("Customer angry");
      }
      // complex logic
   }
}


interface Dependency {
   boolean isOnionAllowed();
   boolean isCucumberAllowed();
}