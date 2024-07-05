package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;


public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  @Test
  void shouldPassValidation() {
    Customer customer = TestData.aValidCustomer();

    validator.validate(customer);
  }

  @Test
  void shouldFailForNullName() {
    Customer customer = TestData.aValidCustomer().setName(null);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void shouldFailForNullEmail() {
    Customer customer = TestData.aValidCustomer().setEmail(null);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }
  @Test
  void shouldFailForNullCity() {
    Customer customer = TestData.aValidCustomer();
    customer.getAddress().setCity(null);
    Assert.assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
  }

  @Test
  void shouldFailForShortCity() {
    Customer customer = TestData.aValidCustomer();
    customer.getAddress().setCity("a");
    Assert.assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
  }

  @Test
  void shouldTrimCity() {
    Customer customer = TestData.aValidCustomer();
    customer.getAddress().setCity("  abc  ");
    validator.validate(customer);
    Assert.assertEquals("abc", customer.getAddress().getCity());
  }

}