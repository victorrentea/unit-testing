package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FastFood {
   private final Dependency dependency;
// think: break prod
   public void makeShawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException("Unconceivable!!!!!(*%#@(*&%*(!^&%(");
      }
      // complex logic >= 7 if
   }

   public void makeTzatziki() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic >= 5 Imagine Dragons
   }
}


interface Dependency {
   boolean isOnionAllowed();
   boolean isCucumberAllowed();
}