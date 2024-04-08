package victor.testing.mutation;

// Object Mother Pattern https://martinfowler.com/bliki/ObjectMother.html
public class TestData {
  public static Customer validCustomer() {
    return new Customer()
        .setName("::name::")
        .setEmail("::email::")
        .setAddress(new Address()
            .setCity("::city::"));
  }
}
