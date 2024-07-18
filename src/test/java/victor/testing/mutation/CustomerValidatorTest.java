package victor.testing.mutation;


import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


//@TestInstance(TestInstance.Lifecycle.PER_CLASS) only for epic huge tests
public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  Customer customer = new Customer()
      .setName("::name::")
      .setEmail("::email::")
      .setAddress(new Address()
          .setCity("::city::"));

  CustomerValidatorTest() {
    System.out.println("Hola Barcelona! " +
                       "JUnit4+5 instantiate the test class ONCE for every @Test");
  }
  //   @BeforeAll // for integraton tests more.
//   public static void beforeAllTests() {
//      System.out.println("Start class");
//   }
  @Test
  void happy() {validator.validate(customer); }

  @Test
  void failsForMissingName() {
    customer.setName(null);
    assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void failsForMissingEmail() {
    customer.setEmail(null);
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
    assertEquals("Missing customer email", e.getMessage());
    //WerfenException
//    assertEquals(ErrorCode.CUSTOMER_MISSING_EMAIL, e. /getErrorCode());

  }

  @Test
  void failsForMissingCity() {
    customer.getAddress().setCity(null);
    assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }

  @Test
  void failsForCityTooShort() {
    customer.getAddress().setCity("Bo");
    assertThrows(IllegalArgumentException.class,
        () -> validator.validate(customer));
  }


}