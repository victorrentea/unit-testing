package victor.testing.mutation;


import lombok.Builder;
import lombok.With;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


@With // "wither"
@Builder(toBuilder = true)
record CustDto(String name, String email) {
//  public CustDto withName(String name) {
//    return this.name == name ? this : new CustDto(name, email);
//  }
}

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) only for epic huge tests
public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  Customer customer = new Customer()
      .setName("::name::")
      .setEmail("::email::")
      .setAddress(new Address()
          .setCity("::city::"));


  public static CustDto.CustDtoBuilder aCust() {
    return CustDto.builder()
        .name("::name::")
        .email("::email::");
  }

  public void actualTest() {
//    CustDto tweaked = aCust().toBuilder().name("diff").build();
    CustDto tweaked = aCust().name("diff").build(); // better
    CustDto cloned = tweaked.withName("cloned") // less to write
        .withEmail("diff"); // + mem /cpu effort (malloc clone) x 2
  }
//  private Customer.Builder ifCustomerWereImmutable() {

  CustomerValidatorTest() {
    System.out.println("Hola Barcelona! " +
                       "JUnit4+5 instantiate the test class ONCE for every @Test");
  }
  //   @BeforeAll // for integraton tests more.
//   public static void beforeAllTests() {
//      System.out.println("Start class");
//   }
  @Test
  void happy() {
    // given: context
    customer.getAddress().setCity("  ACity   ");

    // when: act to prod
    validator.validate(customer);

    // then: effects produced
    // pragmatic: put several asserts in a @Test
//    assertEquals("ACity", customer.getAddress().getCity());
    assertThat(customer.getAddress().getCity())
        .isEqualTo("ACity");
  }
//  @Test
//  void trimsCity() {
//    customer.getAddress().setCity("  ACity   ");
//
//    // when
//    validator.validate(customer);
//
//    assertEquals("ACity", customer.getAddress().getCity());
//  }

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