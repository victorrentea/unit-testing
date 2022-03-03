package victor.testing.designhints.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Kebab {
   private final Dependency dependency;

   public void complex1() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic
   }
}
@RequiredArgsConstructor
class KebabComplex2 {
   private final Dependency dependency;
   public void complex2() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic
   }
}


interface Dependency {
   boolean isOnionAllowed();
   boolean isCucumberAllowed();
}