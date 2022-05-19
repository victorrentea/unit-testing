package victor.testing.designhints.functionalcore;


import com.google.common.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;

public class ImperativeShell {
   private final FunctionalCore functionalCore;
   private final RepoA repoA;
   private final RepoB repoB;
   private final RepoC repoC;

   public ImperativeShell(FunctionalCore functionalCore, RepoA repoA, RepoB repoB, RepoC repoC) {
      this.functionalCore = functionalCore;
      this.repoA = repoA;
      this.repoB = repoB;
      this.repoC = repoC;
   }

   public void case51(long idA, long idB) {
      A a = repoA.findById(idA);
      B b = repoB.findById(idB);

      C c = theLogic(a, b);

      repoC.save(c);
   }


   @NotNull
   @VisibleForTesting
   C theLogic(A a, B b) { //pure
      // complex logic requiring 7 tests
      String computedData = "computed with " + a + b;
      return new C(computedData);
   }
}

class FunctionalCore {

}
