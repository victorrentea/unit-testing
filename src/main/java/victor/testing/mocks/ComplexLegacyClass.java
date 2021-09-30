package victor.testing.mocks;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class ComplexLegacyClass {

   public Optional<String> publicComplex(int id) {
      System.out.println("Complex code #1"); // TODO test

      List<String> data = privateComplex(id);
      if (data.isEmpty()) {
         return empty();
      }
      String first = data.get(0);
      System.out.println("Complex code #2"); // TODO test
      return ofNullable(first);
   }

   List<String> privateComplex(int id) {
      System.out.println("Complex code I don't want to run in my test");

      List<String> list = null;
      // or a call that can't run in unit tests
      if (true) throw new RuntimeException("Can't call this REST APIs from tests");
      return list;
   }
}

//On my job we werent allowed to change private
// methods to package protected when testing. So we had to use PowerMock. Would you recomment something like that?