package victor.testing.builder;

// ditamai patternul
// https://martinfowler.com/bliki/ObjectMother.html#:~:text=An%20object%20mother%20is%20a,a%20lot%20of%20example%20data.
public class TestData {
   public static Customer aValidCustomer() {
      return new Customer()
          .setName("::name::")
          .setEmail("::email::")
          .setAddress(new Address()
              .setStreetName("Stefan cel Mare")
              .setCity("::oras::"));
   }
}
