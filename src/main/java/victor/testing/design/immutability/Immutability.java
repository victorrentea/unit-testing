package victor.testing.design.immutability;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
class A {
   private final B b;
   private final ObjRepo objRepo;
   public String caller(long id) {
      Obj obj = objRepo.findById(id);
      b.method(obj);
      var hackedObj = obj.toBuilder().y("s").build();
      // logic
      System.out.println("Logic using " + obj.x());
      return obj.x();
   }
}
class B {
   public void method(Obj obj) {
      // logic
      String x = "computed like in prod";
      // logic
   }
}

@Builder(toBuilder = true)
record Obj(String x, String y) {
}

interface ObjRepo {
   Obj findById(long id);
}
