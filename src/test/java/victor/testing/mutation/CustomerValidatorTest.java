package victor.testing.mutation;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  public CustomerValidatorTest() {
    System.out.println("👶");
  }
  //morala tot ce lasi date stricate pe campurile clasei de test se reseteaza pt urmatorul @Test
  Customer customer = new Customer()
      .setName("::name::")
      .setEmail("::email::")
      .setAddress(new Address()
          .setCity("::city::"));

  @Test
  void valid() {
    validator.validate(customer);
  }

  @Test
  void throwsForMissingName() {
    customer.setName(null);

    Assert.assertThrows(IllegalArgumentException.class,
        ()->validator.validate(customer));
  }

  @Test
  void throwsForMissingEmail() {
    customer.setEmail(null);

    Assert.assertThrows(IllegalArgumentException.class ,
        ()->validator.validate(customer));
  }
}