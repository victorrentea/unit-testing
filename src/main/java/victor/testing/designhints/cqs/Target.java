package victor.testing.designhints.cqs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Target {
   private final Dependency dependency;

   public void testedMethod(Obj obj) {
      // logic to test
      dependency.command(obj, 5);
      int x = dependency.query(obj, 5);
      System.out.println("Logic with " + x);
   }
}

class Dependency {
   public void command(Obj obj, int x) {
      // imagine complexity => tested separately
      obj.setTotal(obj.getTotal() + x); // side effect ==> command
   }
   public int query(Obj obj, int x) {
      return x * 2; // returns ==> query
   }
}

@Data
class Obj {
   private int total;
}