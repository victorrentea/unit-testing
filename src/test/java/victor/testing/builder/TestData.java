package victor.testing.builder;

import java.util.List;

// Object MOther pattern https://martinfowler.com/bliki/ObjectMother.html
public class TestData {
   public static Customer aValidCustomer() {
      return new Customer()
          .setName("::name::")
          .setEmail("a@b.com")
          .setLabels(List.of("label1"))
          .setAddress(new Address()
              .setCity("::city::"));
   }
}
