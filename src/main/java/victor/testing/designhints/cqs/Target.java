package victor.testing.designhints.cqs;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;

@RequiredArgsConstructor
public class Target {
   private final Dependency dependency;

   public void testedMethod(Obj obj) {
      // logic to test
      Obj copy = dependency.derive(obj, 5);
      // obj.total = 5
      int x = dependency.query(copy.getTotal());
      System.out.println("Logic with " + x);
   }

   public static void main(String[] args) {
      new Target(new Dependency()).testedMethod(new Obj(0));
   }
}

class Dependency {
   public int query(int x) {
      return x * 2; // returns ==> query
   }
   public Obj derive(Obj obj, int x) {
      return obj.withTotal(obj.getTotal() + x); // side effect ==> command
   }
}

@Data
class Obj {
   @With
   private final int total;
}