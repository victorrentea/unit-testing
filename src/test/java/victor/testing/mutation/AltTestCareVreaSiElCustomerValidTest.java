package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static victor.testing.mutation.TestData.validCustomer;


public class AltTestCareVreaSiElCustomerValidTest {
   CustomerValidator validator = new CustomerValidator();

   Customer customer = validCustomer();

   @Test
   void valid() {
      validator.validate(customer);
   }

}