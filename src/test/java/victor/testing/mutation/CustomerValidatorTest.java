package victor.testing.mutation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tools.HumanReadableTestNames;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(HumanReadableTestNames.class)
//public class CustomerValidatorShould { // tends to scare devs out because they search CVT
public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  private Customer customer = new Customer()
      .setName("::name::")
      .setEmail("         ")
      .setAddress(new Address()
          .setCity("::city::"));

  public CustomerValidatorTest() {
    System.out.println("By default JUnit instantiates the test class once per each @Test");
    // moral: don't ever be afraid of leaving 'mutated/corrupted/dirty' data on fields
  }

  @Test
  void valid() {
    validator.validate(customer);
  }

  @Test
  void trimsCityName() {
    customer.getAddress().setCity("  name    ");

    validator.validate(customer); // prod call

//    assertEquals("name", customer.getAddress().getCity()); // never, not AssertJ jupiter.Assertions; that's old....
//    assertThat(customer.getAddress().getCity()).isEqualTo("  name    ".trim()); // repeats logic in prod; NO
    assertThat(customer.getAddress().getCity()).isEqualTo("name");
  }

  //  @DisplayName("if a customer with a null name is validated, it throw an exception")
  // "//comments are bad if you CAN refactor the code to be self-describing" <- Clean Code
  // they can become outdated. out of sync with the test
//   void notValid_ifNameIsNull() {
//  void invalidWhenNameIsNull() {
//  void givenACustoemrWithNullName_validatioFails() {
  @Test
  void throwForNullName() { // naming convention to start with the EFFECT expected
    customer.setName(null);

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Missing customer name");
  }

  @Test
  void throwForNullEmail() {
    customer.setEmail(null);

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Missing customer email");
  }

  @Test
  void throwForNullAddressCity() {
    customer.getAddress().setCity(null);

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Missing address city");
  }

  @Test
  void throwForAddressCityTooShort() {
    customer.getAddress().setCity("12");

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Address city too short");
  }



  //  This is what I thought you meant by a test that would fail if you remove the "trim" from production code (line 20 on validator):
  @Test
  void throwForAddressCityTooShortAfterTrimming() {
    customer.getAddress().setCity(" a ");
    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Address city too short");
  }

  // what if customer, or customer.address is null : not a product-relevant behavior-> not test IS REQUIRED
}