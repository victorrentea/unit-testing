package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

//  @Rule // Junit 4
//  public ExpectedException thrown = ExpectedException.none(); // fields in thrown Exception

  @Test
  //@ExpectedException(IllegalArgumentException.class) // Junit 4 - just the type
  void failsForNullEmail() {
    customer.setEmail(null)/*.setName(null)*/;

    IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, // Junit 4 also
        () -> validator.validate(customer));
//    assertEquals("Missing customer email", e.getMessage());
    assertTrue(e.getMessage().contains("email"));
    // if the exception message is exposed to User/Client then assert it exactly in the @Test
    // MyValidationException e =assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
    // assertEquals(ErrorCode.MISSING_EMAIL, e.getErrorCode()); // love!

    // or
    // assertThrows(CustomerMissingNameException.class, () -> validator.validate(customer)); // hate
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

  @Test
  void trimCity() {
    // given
    customer.getAddress().setCity("  Timisoara  ");

    // when
    validator.validate(customer);

    // then
    assertEquals("Timisoara", customer.getAddress().getCity());
  }

  @Test
  void failsForNullCustomer() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
    assertEquals("Missing customer", e.getMessage());
    // a) isn't this impossible in the context where the validator is used?
    // b) what SHOULD the validator do for null?
    // ==> exception more expressive than a NPException
    // let's assume you want to throw an ex with a nice message
  }
}