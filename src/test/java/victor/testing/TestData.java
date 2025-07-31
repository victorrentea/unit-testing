package victor.testing;

import victor.testing.mutation.Address;
import victor.testing.mutation.Customer;

public class TestData {
  public static Customer aCustomer() { // folosita de 50 @Test
    // aici NU editezi nimic, doar adaugi campuri/metode
    // daca nu-ti pasa de 80% din prop, poti parsa ob dintr-un json din /src/test/resources
    return new Customer()
        .setName("::name::")
        .setEmail("::email::")
        .setAddress(new Address()
            .setCity("::city::"));
  }
}
