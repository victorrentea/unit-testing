package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   private Customer aCustomer() {// canonical data
      return new Customer()
          .setName("::name::")
          .setEmail("::email::")
          .setAddress(new Address()
              .setCity("::city::"));
   }

   @Test
   void valid() {
//      dto.toBuilder().withName(null).build();
      Customer customer = aCustomer();
      validator.validate(customer);
   }

   @Test
   void failsForMissingName() {
      Customer customer = aCustomer().setName(null);
      assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
   }
   @Test
   void failsForMissingEmail() {
      Customer customer = aCustomer().setEmail(null);
      assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
   }
//   @Test
//void failsForMissingCity() {
//      Customer customer = aCustomer().getAddress().setCity(null);
//      assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
//   }


}