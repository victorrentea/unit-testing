package victor.testing.mutation;


import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tools.HumanReadableTestNames;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@DisplayNameGeneration(HumanReadableTestNames.class)

public class CustomerValidatorShould {
  CustomerValidator validator = new CustomerValidator();

  @Test
  void happy() {
    Customer customer = TestData.validCustomer();

    assertDoesNotThrow(() -> validator.validate(customer));
  }
  @Test
  void trimsCity() {
    Customer customer = TestData.validCustomer()
        .setAddress(new Address()
            .setCity("  ::city:: "));

    validator.validate(customer);

    assertThat(customer.getAddress().getCity())
        .isEqualTo("::city::");
  }
  @Test
  void trimsName() {
    Customer customer = TestData.validCustomer().setName(" ::name::  ");

    validator.validate(customer);

    assertThat(customer.getName())
        .isEqualTo("::name::");
  }

  @Test
  void throwsDueToMissingName() {
    Customer customer = TestData.validCustomer().setName(null);

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void throwsDueToMissingEmail() {
    Customer customer = TestData.validCustomer().setEmail(null);

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Missing customer email");
  }
  @Test
  void throwsDueToMissingAddressCity() {
    Customer customer = TestData.validCustomer()
        .setAddress(new Address()
            .setCity(null));

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class);
  }
  @Test
  void throwsDueToAddressCityTooShort() {
    Customer customer = TestData.validCustomer()
        .setAddress(new Address()
            .setCity("AB"));

    assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class);
  }
}