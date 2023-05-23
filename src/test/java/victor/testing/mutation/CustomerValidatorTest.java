package victor.testing.mutation;


import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   private static Customer validCustomer() {
      return new Customer()
          .setName("::name::")
          .setEmail("::email::")
          .setAddress(new Address()
              .setCity("::city::"));
   }
   Customer customer;
   @BeforeEach
   final void before() {
       customer = validCustomer();
   }

   @Test
   void valid() {
      validator.validate(customer);
   }

   @Test
       // givenAAcustomer_whenHerNameIsNull_thenThrowsException
//   void invalidCustomerName_throwsException() {
//   void throwsExceptionWhenInvalidCustomerName() {
//   void throwsWhenInvalidCustomerName() {
//   void throwsForInvalidCustomerName() {
//   void throwsForInvalidName() {
   void throwsForNullName() {
      customer.setName(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void throwsForNullEmail() {
      customer.setEmail(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void throwsForNullAddressCity() {
      customer.getAddress().setCity(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

}