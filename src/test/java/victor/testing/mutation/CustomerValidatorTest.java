package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.Assert.assertThrows;

//@TestInstance(Lifecycle.PER_CLASS) // avoid
// test class
public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();
  Customer customer = new Customer()
        .setName("::name::")
        .setEmail("::email::")
        .setAddress(new Address()
            .setCity("::city::"));

  CustomerValidatorTest() {
    System.out.println("a new instance of CustomerValidatorTest is created for each test method");
    // any data you leave on the fields of the test class is recreated for the next one
  }

  @Test
//   void passesValidation() {
  void ok() {
    validator.validate(customer);
  }

  // given = context: when.thenReturn, setup date, insert, ...
  // when = prod call (signal): method call, event, message, etc
  // then = assert (response): return value, state change, verify
//   void customerNameIsNull() {
//   void whenCustomerNameIsNull_thenValidationFails() {  // Magical number 7±2
//   void failsForNullCustomerName() {
//   void testCustomerNameNull()
  @Test
  void failsForNullName() { // we begin with the "then" part, and then we go to the "when" part
    customer.setName(null);

    assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void failsForNullEmail() {
    customer.setEmail(null);

    assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void failsForNullCity() {
    customer.getAddress().setCity(null);

    assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void failsForShortCity() {
    customer.getAddress().setCity("12");

    assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }
}