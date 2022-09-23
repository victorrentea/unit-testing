package victor.testing.design.immutability;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class A {
   private final B b;
   private final ObjRepo objRepo;
   public String caller(long id) {
      Obj obj = objRepo.findById(id);
      b.method(obj);
      // logic
      System.out.println("Logic using " + obj.getX());
      return obj.getX();
   }
}
class B {
   public void method(Obj obj) {
      // logic
      String x = "computed like in prod";
      obj.setX(x);
      // logic
   }
}

@Data
class Obj {
   private String x;
}

interface ObjRepo {
   Obj findById(long id);
}
