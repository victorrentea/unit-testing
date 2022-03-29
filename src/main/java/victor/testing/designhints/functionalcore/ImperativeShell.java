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

      C c = functionalCore.pureFunction(a, b);

      repoC.save(c);
   }

}

class FunctionalCore {
   public C pureFunction(A a, B b) {
      // complex logic requiring 7 tests
      String computedData = "computed with " + a + b;
      return new C(computedData);
   }
}
