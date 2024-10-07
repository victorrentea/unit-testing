package victor.testing.mutation;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerValidatorParameterizedTest {
  CustomerValidator validator = new CustomerValidator();

  public static Customer newValidCustomer() {
    Customer aCustomer = new Customer();
    aCustomer.setName("::name::");
    aCustomer.setEmail("::email::");
    aCustomer.setAddress(new Address());
    aCustomer.getAddress().setCity("::city::");
    return aCustomer;
  }
  public static Stream<Arguments> data() {
    return Stream.of(
        Arguments.of(newValidCustomer(), true),
        Arguments.of(newValidCustomer().setName(null), false),
        Arguments.of(newValidCustomer().setEmail(null), false),
        Arguments.of(newValidCustomer().setAddress(new Address().setCity(null)), false)
    );
  }

  @ParameterizedTest
  @MethodSource("data")
  void valid(Customer aCustomer, boolean ok) {
    if (ok) {
      validator.validate(aCustomer);
    } else {
      assertThatThrownBy(() -> validator.validate(aCustomer))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }
}