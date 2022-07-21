package victor.testing.mutation;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

class TestData {
   // e prea tehnica.
   public static Customer john() {
      return new Customer()
              .setName("::name::")
              .setEmail("::email::")
              .setAddress(new Address()
                      .setCity("::city::"));
   } // Object Mother

}

public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void happy() {
      Customer customer = TestData.john();
      validator.validate(customer);
   }

   @Test
   void throws_forMissingCustomerName() {
      Customer customer = TestData.john().setName(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class);
   }
   @Test
   void throws_forMissingCustomerEmail() {
      Customer customer = TestData.john().setEmail(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class);
   }



}