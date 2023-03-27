package victor.testing.mutation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void valid() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      validator.validate(customer);
   }

   @Test
   void throwForMissingName() {
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      Assertions.assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("Missing customer name")
      ;
   }

}