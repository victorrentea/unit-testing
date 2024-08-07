package victor.testing.mutation;


import org.junit.jupiter.api.Test;


public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  @Test
  void valid() {
    Customer customer = new Customer();
    customer.setName("::name::");
    customer.setEmail("::email::");
    customer.setAddress(new Address());
    customer.getAddress().setCity("::city::");
    validator.validate(customer);
  }

}