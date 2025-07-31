package victor.testing.mutation;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import victor.testing.TestData;

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

    Assert.assertThrows(IllegalArgumentException.class,
        ()->validator.validate(customer));
  }

  @Test
  void throwsForMissingEmail() {
    customer.setEmail(null);

    Assert.assertThrows(IllegalArgumentException.class ,
        ()->validator.validate(customer));
  }
}