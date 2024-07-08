package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// test class
public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();
  private Customer customer;

  // @Before in JUnit 4
  @BeforeEach
  final void setup() {
    customer = new Customer()
        .setName("::name::")
        .setEmail("::email::")
        .setAddress(new Address()
            .setCity("::city::"));
  }

  @Test
//   void passesValidation() {
  void ok() {
    validator.validate(customer);
  }

  // given = context: when.thenReturn, setup date, insert, ...
  // when = prod call (signal): method call, event, message, etc
  // then = assert (response): return value, state change, verify
  @Test
//   void customerNameIsNull() {
//   void whenCustomerNameIsNull_thenValidationFails() {  // Magical number 7±2
//   void failsForNullCustomerName() {
//   void testCustomerNameNull()
  void failsForNullName() { // we begin with the "then" part, and then we go to the "when" part
    customer.setName(null);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void failsForNullEmail() {
    customer.setEmail(null);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void failsForNullCity() {
    customer.getAddress().setCity(null);

    Assert.assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void failsForShortCity() {
    customer.getAddress().setCity("12");

    Assert.assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }
}