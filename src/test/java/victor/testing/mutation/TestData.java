package victor.testing.mutation;

public class TestData {
  static public Customer aCustomer() {
    return new Customer()
        .setName("::name::") // NEVER EDIT this!, only populate NEW fields or ADD new methods
        .setEmail("a@b.com")
        .setAddress(new Address()
            .setCity("::city::"));
  }
}
