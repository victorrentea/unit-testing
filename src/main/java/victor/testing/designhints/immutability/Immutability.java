package victor.testing.designhints.immutability;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;

@RequiredArgsConstructor
class A {
   private final B b;
   private final ObjRepo objRepo;

   public String caller(long id) {
      Obj obj = objRepo.findById(id);
      String x = b.method(obj);
//      obj = obj.withX(x);

      // logic
      System.out.println("Logic using " + x);
      return x;
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
   @With
   private final String x;
}

interface ObjRepo {
   Obj findById(long id);
}
