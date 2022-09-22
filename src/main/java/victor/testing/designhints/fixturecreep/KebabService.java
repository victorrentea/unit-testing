package victor.testing.designhints.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KebabService {
   private final Dependency dependency;

   public void shawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }

      // complex logic 10 x if
   }

}
@RequiredArgsConstructor
class TatzikiService  {
   private final Dependency dependency;

   public void tzatziki() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic 7 tests you need
   }
}


interface Dependency {
   boolean isOnionAllowed();
   boolean isCucumberAllowed();
}