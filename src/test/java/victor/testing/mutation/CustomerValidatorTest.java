package victor.testing.mutation;

import org.junit.jupiter.api.Test;
import victor.testing.TestData;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

  public CustomerValidatorTest() {
    System.out.println("👶");
  }
  //morala tot ce lasi date stricate pe campurile clasei de test se reseteaza pt urmatorul @Test
  Customer customer = TestData.aCustomer();

  @Test
  void valid() {
    validator.validate(customer);
  }

  @Test
//  void givenCustomerHasNullName_whenValidated_throwsException() {
  void throwsForMissingName() { // ce efect, cand se intampla
    customer.setName(null);

    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> validator.validate(customer));
  }

  @Test
  void throwsForMissingEmail() {
    customer.setEmail(null);

    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> validator.validate(customer));
  }
}