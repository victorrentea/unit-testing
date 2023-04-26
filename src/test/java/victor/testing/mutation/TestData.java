package victor.testing.mutation;

// Object Mother
public class TestData {
  static Address anAddress() {
     return new Address()
             .setStreetName("Mihai Bravu")
             .setCity("::city::");
  }

  static Customer aCustomer() {
     return new Customer()
             .setName("::name::")
             .setEmail("::email::")
             .setAddress(anAddress());
  }
}
