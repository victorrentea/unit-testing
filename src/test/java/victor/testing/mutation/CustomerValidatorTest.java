package victor.testing.mutation;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  @Test
  void valid() {
    Customer customer = validCustomer();

    validator.validate(customer);
  }

  private Customer validCustomer() {
    return new Customer()
        .setName("::name::")
        .setEmail("::email::")
        .setAddress(new Address()
            .setCity("::city::"));
  }

  @Test
  void throwsForMissingName() {
    Customer customer = validCustomer().setName(null);

    Assert.assertThrows(IllegalArgumentException.class ,
        ()->validator.validate(customer));
  }

  @Test
  void throwsForMissingEmail() {
    Customer customer = validCustomer().setEmail(null);

    Assert.assertThrows(IllegalArgumentException.class ,
        ()->validator.validate(customer));
  }
}