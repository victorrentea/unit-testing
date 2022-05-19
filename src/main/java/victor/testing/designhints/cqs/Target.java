package victor.testing.designhints.cqs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Target {
   private final Dependency dependency;

   public void testedMethod(Obj obj) {
      // logic to test
      dependency.changeStuff(obj, 5);
      int x = dependency.computeStuff(5);
      System.out.println("Logic with " + x);
   }
}

class Dependency {
   public int computeStuff(int x) {
      return x * 2; // returns ==> query
   }
   public void changeStuff(Obj obj, int x) {
      obj.setTotal(obj.getTotal() + x); // side effect ==> command
   }
}

@Data
class Obj {
   private int total;
}