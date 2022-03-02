package victor.testing.designhints.functionalcore;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImperativeShell {
   private final FunctionalCore functionalCore;
   private final RepoA repoA;
   private final RepoB repoB;
   private final RepoC repoC;

   public void case51(long idA, long idB) {
      A a = repoA.findById(idA);
      B b = repoB.findById(idB);

      // complex logic requiring 7 tests
      String computedData = "computed with " + a + b;
      C c = new C(computedData);

      repoC.save(c);
   }
}

class FunctionalCore {

}
