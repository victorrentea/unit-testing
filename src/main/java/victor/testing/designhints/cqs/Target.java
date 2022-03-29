package victor.testing.designhints.cqs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Target {
   private final Dependency dependency;

   public void testedMethod(Obj obj) {
      // logic to test

      dependency.command(obj, 5);
      int x = dependency.query(5);
      System.out.println("Logic with " + x);
   }
}

class Dependency {
   public int query(int x) {
      // imagine complexity => tested separately
      return x * 2; // returns ==> query
   }
   public void command(Obj obj, int x) {
      // imagine complexity => tested separately
      obj.setTotal(obj.getTotal() + x); // side effect ==> command
   }
}

@Data
class Obj {
   private int total;
}