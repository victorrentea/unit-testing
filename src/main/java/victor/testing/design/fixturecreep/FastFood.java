package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
 class MakeShawarmaService {
   private final Dependency dependency;

   public void makeShawarma() { // aka kebab
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic: 7 ifs
   }

}
@RequiredArgsConstructor
 class MakeTzatzikiService {
   private final Dependency dependency;

   public void makeTzatziki() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic: 5 ifs
   }
}
