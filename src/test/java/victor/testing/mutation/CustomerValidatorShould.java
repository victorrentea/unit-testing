package victor.testing.mutation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;


public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void validWithCityWithSpaces() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("  ::city:: ");

      validator.validate(customer);

      assertThat(customer.getAddress().getCity())
          .isEqualTo("::city::");
   }

}