package victor.testing.mutation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


//abstract class IntegrationTestBase {
//  @BeforeEach
//  final void before() { // sharing initialization BEHAVIOR
//    // do something before each test
//    // insert
//    // mock when thenReturn
//  }
//}
class ObjectMother {
  // never CHANGE test-data factory methods. only add to them, or new ones
  public static Customer aCustomer() { // reusing test data
    // mandatory when using Immutables
    return new Customer()
            .setName("::name::")
            .setEmail("::email::")
            .setAddress(new Address().setCity("::city::"));

  }
}

public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();
  private Customer validCustomer = ObjectMother.aCustomer();

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