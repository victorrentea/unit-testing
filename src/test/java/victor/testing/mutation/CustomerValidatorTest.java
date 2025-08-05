package victor.testing.mutation;

import org.junit.jupiter.api.Test;

public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  @Test
  void valid() {
    Customer aCustomer = new Customer();
    aCustomer.setName("::name::");
    aCustomer.setEmail("::email::");
    aCustomer.setAddress(new Address());
    aCustomer.getAddress().setCity("::city::");
    validator.validate(aCustomer);
  }

}