package victor.testing.design.objectmother;

import victor.testing.design.objectmother.Customer.CustomerBuilder;

// Object Mother pattern: https://martinfowler.com/bliki/ObjectMother.html
public class TestData {
  public static CustomerBuilder aCustomer() { // canned object
    return Customer.builder()
        .name("John Doe")
        .billingAddress("123 Main St")
        .shippingAddress("anything") // useless details that make the test harder to understand
        .phoneNumber("WHY?!!!");

    // variation if your object is XXX-large,
    // you could parse it from a JSON on disk from src/test/resources/customer.json
  }
}
