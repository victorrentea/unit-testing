package victor.testing.service;

import org.junit.jupiter.api.Test;
import victor.testing.entity.Address;
import victor.testing.entity.Customer;

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