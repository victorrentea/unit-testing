package victor.testing.designhints.immutability;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class A {
   private final B b;
   private final ObjRepo objRepo;

   public String caller(long id) {
      Obj obj = objRepo.findById(id);

      String x = b.method(obj);
      obj.setX(x);

      // logic
      System.out.println("Logic using " + obj.getX());
      return obj.getX();
   }
}

class B {
   public String method(Obj obj) {
      // logic
      String x = "computed like in prod";

      // logic
      return x;
   }
}

@Data
class Obj {
   private String x;
}

interface ObjRepo {
   Obj findById(long id);
}
