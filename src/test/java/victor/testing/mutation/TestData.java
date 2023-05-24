package victor.testing.mutation;

public class TestData {
  public static Customer validCustomer() { // factory method for test data
     Customer customer = new Customer();
     customer.setName("::name::");
     customer.setEmail("::email::");
     customer.setAddress(new Address());
     customer.getAddress().setCity("::city::");
     return customer;
  }
}
