package victor.testing.design.cqs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Target {
   private final Dependency dependency;

   public void testedMethod(Obj obj) {
      // logic to test
      int x = dependency.stuff(obj, 5);
      System.out.println("Logic with " + x);
      // TODO [HARD] how about temporal coupling? --> Immutables
   }
}

class Dependency {

   public int stuff(Obj obj, int x) {
      // imagine complexity => tested separately
      obj.setTotal(obj.getTotal() + x); // side effect ==> command
      return x * 2; // returns ==> query
   }
}

@Data
class Obj {
   private int total;
}