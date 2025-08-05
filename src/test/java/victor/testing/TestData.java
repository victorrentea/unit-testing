package victor.testing;

import victor.testing.mutation.Address;
import victor.testing.mutation.Country;
import victor.testing.mutation.Customer;

// Object Mother
public class TestData {
  // start from a 'standard' data example and tweak it in the test cases to explore various paths through logic
  public static Customer aCustomer() { // used by 53 tests
    return new Customer()
        .setName("::name::")
        .setEmail("::email::")
        // append-only class:
        .setAddress(anAddress());
  }

  public static Address anAddress() {
    return new Address()
        .setCity("::city::")
        .setCountry(Country.FRA)
        .setStreetName("Diagonal")
        .setStreetNumber(42);
  }
}
