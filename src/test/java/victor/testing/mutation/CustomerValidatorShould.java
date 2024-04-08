package victor.testing.mutation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

   @Test
   void throwsDueToNullName() {
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("  ::city:: ");

      assertThatThrownBy(()->validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class);
   }
@Test
   void throwsDueToNullEmail() {
      Customer customer = new Customer();
      customer.setAddress(new Address());
      customer.getAddress().setCity("  ::city:: ");

      assertThatThrownBy(()->validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing customer email");
   }
}