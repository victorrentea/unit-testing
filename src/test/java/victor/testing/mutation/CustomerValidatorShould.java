package victor.testing.mutation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tools.HumanReadableTestNames;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@DisplayNameGeneration(HumanReadableTestNames.class)

public class CustomerValidatorShould {
  CustomerValidator validator = new CustomerValidator();

  private Customer validCustomer() {
    return new Customer()
        .setName("::name::")
        .setEmail("::email::")
        .setAddress(new Address()
            .setCity("::city::"));
  }

  @Test
  void happy() {
    Customer customer = validCustomer();

    assertDoesNotThrow(() -> validator.validate(customer));
  }
  @Test
  void trimsCity() {
    Customer customer = validCustomer()
        .setAddress(new Address()
            .setCity("  ::city:: "));

    validator.validate(customer);

    assertThat(customer.getAddress().getCity())
        .isEqualTo("::city::");
  }
  @Test
  void trimsName() {
    Customer customer = validCustomer().setName(" ::name::  ");

    validator.validate(customer);

    assertThat(customer.getName())
        .isEqualTo("::name::");
  }

  @Test
  void throwsDueToMissingName() {
    Customer customer = validCustomer().setName(null);

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void throwsDueToMissingEmail() {
    Customer customer = validCustomer().setEmail(null);

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Missing customer email");
  }
  @Test
  void throwsDueToMissingAddressCity() {
    Customer customer = validCustomer()
        .setAddress(new Address()
            .setCity(null));

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class);
  }
  @Test
  void throwsDueToAddressCityTooShort() {
    Customer customer = validCustomer()
        .setAddress(new Address()
            .setCity("AB"));

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class);
  }
}