package victor.testing.designhints.functionalcore;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImperativeShell {
   private final FunctionalCore functionalCore;
   private final RepoA repoA;
   private final RepoB repoB;
   private final RepoC repoC;

   public void useCase7(long idA, long idB) {
      A a = repoA.findById(idA);
      B b = repoB.findById(idB);

      // complex logic requiring 7 tests
      String computedData = "via complex logic";
      C c = new C(computedData);

      repoC.save(c);
   }
}

class FunctionalCore {

}

class A{}
interface RepoA {
   A findById(long id);
}
class B{}
interface RepoB {
   B findById(long id);
}
class C{
   private final String data;
   public C(String data) {
      this.data = data;
   }
   public String getData() {
      return data;
   }
}
interface RepoC {
   void save(C c);
}