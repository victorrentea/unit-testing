package victor.testing.mutation;


import org.junit.jupiter.api.Test;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void valid() {
      Customer customer = new Customer()
          .setName("::name::")
          .setEmail("::email::")
          .setAddress(new Address()
              .setCity("::city::"));
      validator.validate(customer);
   }
   @Test
   void failsForMissingName() {
      Customer customer = new Customer()
          .setEmail("::email::")
          .setAddress(new Address()
              .setCity("::city::"));
      validator.validate(customer);
   }

}