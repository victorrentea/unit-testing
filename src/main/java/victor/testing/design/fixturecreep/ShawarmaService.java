package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShawarmaService {
   private final Dependency dependency;

   public void makeShawarma() {
      if (!dependency.isOnionAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic: 7 ifs
   }

}
