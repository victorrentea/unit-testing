package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;


public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  @Test
  void shouldPassValidation() {
    Customer customer = new Customer();
    customer.setName("::name::");
    customer.setEmail("::email::");
    customer.setAddress(new Address());
    customer.getAddress().setCity("::city::");

    validator.validate(customer);
  }

  @Test
  void shouldFailForNullName() {
    Customer customer = new Customer();
    customer.setEmail("::email::");
    customer.setAddress(new Address());
    customer.getAddress().setCity("::city::");

    Assert.assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void shouldFailForNullEmail() {
    Customer customer = new Customer();
    customer.setName("::name::");
    customer.setAddress(new Address());
    customer.getAddress().setCity("::city::");

    Assert.assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

}