package victor.testing.mutation;


import org.junit.jupiter.api.Test;

import static org.junit.Assert.fail;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void happy() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      validator.validate(customer);
   }
   @Test
//   void whenCustomerNameNull_thenExceptionIsThrown() {
   void throws_forMissingCustomerName() {
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      try {
         validator.validate(customer);
         // nu s-a aruncat
         fail("Trebuia ex");
      } catch (IllegalArgumentException e) {
         return;
      } catch (Exception e) {
         fail("Ex e gresita");
      }
   }
   @Test
   void throws_forMissingCustomerEmail() {
      Customer customer = new Customer();
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      validator.validate(customer);
   }

}