package victor.testing.designhints.cqs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Target {
   private final Dependency dependency;

   public void testedMethod(Obj obj) {
      // logic to test
      int x = dependency.query(5);
      dependency.command(obj, 5);
      System.out.println("Logic with " + x);
   }
}

class Dependency {
   public int query(int x) {
      return x * 2; // returns ==> query
   }
   public void command(Obj obj, int x) {
      obj.setTotal(obj.getTotal() + x); // side effect ==> command
//      repo.insert()
   }
}

@Data
class Obj {
   private int total;
}