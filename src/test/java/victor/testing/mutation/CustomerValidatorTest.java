package victor.testing.mutation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();
  private Customer validCustomer = new Customer()
          .setName("::name::")
          .setEmail("::email::")
          .setAddress(new Address().setCity("::city::"));

  @Test
  void valid() {
    validator.validate(validCustomer);
  }

  @Test
  void throwForMissingName() {
    Customer customer = validCustomer.setName(null);
    assertThatThrownBy(() -> validator.validate(customer))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Missing customer name")
    ;
  }


  @Test
  void throwForMissingEmail() {
    //      Customer customer =validCustomer.builder().withEmail(null).build();
    Customer customer = validCustomer.setEmail(null);
    assertThatThrownBy(() -> validator.validate(customer))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Missing customer email")
    ;
  }

  @Test
  void throwForMissingCity() {
    validCustomer.getAddress().setCity(null);
    assertThatThrownBy(() -> validator.validate(validCustomer))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Missing address city")
    ;
  }

  @Test
  void throwForTooShortCity() {
    validCustomer.getAddress().setCity("12");
    assertThatThrownBy(() -> validator.validate(validCustomer))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Address city too short")
    ;
  }

  @Test
  void trimAddressCity() {
    validCustomer.getAddress().setCity(" 123 ");
    validator.validate(validCustomer);
    assertThat(validCustomer.getAddress().getCity())
            .isEqualTo("123");
  }

}