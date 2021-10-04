package victor.testing.mocks.thenCallback;

import java.util.ArrayList;
import java.util.List;

public class ProdCode {
   private final AnotherClassInYourCode anotherClass;

   public ProdCode(AnotherClassInYourCode anotherClass) {
      this.anotherClass = anotherClass;
   }

   public String methodToTest() {
      // heavy logic
      List<String> accumulator = new ArrayList<>();
      anotherClass.collectMore(accumulator); // expected to add new entries to map
      return String.join("\n", accumulator);
   }
}

class AnotherClassInYourCode {

   public void collectMore(List<String> accumulator) {
      // heavy logic, tested separately
      accumulator.add("Stuff"); // & more
   }
}
