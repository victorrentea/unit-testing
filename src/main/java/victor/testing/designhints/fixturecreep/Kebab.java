package victor.testing.designhints.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Kebab {
   private final Dependency dependency;

   public void shawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic 7 ifuri
      // if
      // if
      // if
      // if
      // if
      // if
      // if
   }

   public void tzatziki() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic 5 ifuri
   }
}


interface Dependency {
   boolean isOnionAllowed();
   boolean isCucumberAllowed();
}