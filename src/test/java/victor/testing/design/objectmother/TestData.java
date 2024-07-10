package victor.testing.design.objectmother;

import victor.testing.design.objectmother.Customer.CustomerBuilder;

// Object Mother pattern: https://martinfowler.com/bliki/ObjectMother.html
public class TestData {
  public static CustomerBuilder aCustomer() { // canned object
    return Customer.builder()
//        .name("John Doe")
        .name("Jane Doe") // changing the object mother contenst can break other test classes that depend on this method
        // RULE: never change but only add stuff to this class=> such classes can exceed 2000 LOC
        .billingAddress("123 Main St")
        .shippingAddress("anything") // useless details that make the test harder to understand
        .phoneNumber("WHY?!!!");

    // variation if your object is XXX-large,
    // you could parse it from a JSON on disk from src/test/resources/customer.json
  }
}
