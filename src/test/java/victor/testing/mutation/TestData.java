package victor.testing.mutation;

// Object Mother
public class TestData {
  // 120 @Tests using this method < high coupling
  public static Customer aCustomer() { // factory method for test data
     Customer customer = new Customer();
     customer.setName("::name::");
     customer.setEmail("::email::");// NEVER CHANGE ANYTHING IN A METHOD of an Object Mother!!
    // only add method, lines, or TWEAK THE RESULTS GOT FROM THE MOTHER
     customer.setAddress(new Address());
     customer.getAddress().setCity("::city::");
     return customer;
  }

  /**
   * John is a 'persona' of a employee that just got hired last week.
   */
  public static Customer charles() { // 'persona' of a customer with infinite budget
     Customer customer = new Customer();
     customer.setName("::name::");
     customer.setEmail("::email::");
     customer.setAddress(new Address());
     customer.getAddress().setCity("::city::");
     return customer;
  }
}
