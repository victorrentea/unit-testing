package victor.testing.mutation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerAnotherTest {
   CustomerValidator validator = new CustomerValidator();
   Customer customer;

   @BeforeEach // run code before every @Test
   final void fixture() {
      customer = TestData.validCustomer();
   }

   @Test
   void acceptsValidCustomer() {
      validator.validate(customer);
   }


}