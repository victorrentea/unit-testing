package victor.testing.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.entity.Customer;
import victor.testing.tools.PrettyTestNames;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
@DisplayNameGeneration(PrettyTestNames.class)
public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  CustomerValidatorTest() {
    System.out.println("new instance of CustomerValidatorTest");
  }
  Customer aCustomer = TestData.aValidCustomerForEveryone();

  @Test
  void valid() {
    validator.validate(aCustomer);
  }

  @Test
  void invalidName() {
    aCustomer.setName(null);
    assertThrows(IllegalArgumentException.class, () -> {
      validator.validate(aCustomer);
    });
  }

  @Test
  void invalidEmail() {
    aCustomer.setEmail(null);
    assertThrows(IllegalArgumentException.class, () -> {
      validator.validate(aCustomer);
    });
  }

  @Test
  void invalidCity() {
    aCustomer.getAddress().setCity(null);
    assertThrows(IllegalArgumentException.class, () -> {
      validator.validate(aCustomer);
    });
  }

  @Test
  void cityTooShort() {
    aCustomer.getAddress().setCity("a");
    assertThrows(IllegalArgumentException.class, () -> {
      validator.validate(aCustomer);
    });
  }

  @Test
  void checkTheCityNameIsTrimmed() {
    aCustomer.getAddress().setCity("  \txyz  ");
    validator.validate(aCustomer);
    assertThat(aCustomer.getAddress().getCity()).isEqualTo("xyz");
  }
}