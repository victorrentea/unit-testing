package victor.testing.designhints.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Shawarma {
   private final Dependency dependency;

   public void shaworma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic  7 ifuri
   }

}


interface Dependency {
   boolean isOnionAllowed();
   boolean isCucumberAllowed();
}