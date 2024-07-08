package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;

// test class
public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
//   void passesValidation() {
   void ok() {
     Customer customer = new Customer()
         .setName("::name::")
         .setEmail("::email::")
         .setAddress(new Address()
             .setCity("::city::"));

      validator.validate(customer);
   }

   // given = context: when.thenReturn, setup date, insert, ...
   // when = prod call (signal): method call, event, message, etc
   // then = assert (response): return value, state change, verify
   @Test
//   void customerNameIsNull() {
//   void whenCustomerNameIsNull_thenValidationFails() {  // Magical number 7±2
//   void failsForNullCustomerName() {
   void failsForNullName() { // we begin with the "then" part, and then we go to the "when" part
//   void testCustomerNameNull()
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::"); // repeats 3 times.

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void failsForNullEmail() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }
   @Test
   void failsForNullCity() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

    @Test
    void failsForShortCity() {
        Customer customer = new Customer();
        customer.setName("::name::");
        customer.setEmail("::email::");
        customer.setAddress(new Address());
        customer.getAddress().setCity("a");

        Assert.assertThrows(IllegalArgumentException.class,
            ()->validator.validate(customer));
    }
}