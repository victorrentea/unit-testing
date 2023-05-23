package victor.testing.mutation;


import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @NotNull
   private static Customer validCustomer() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      return customer;
   }

   @Test
   void valid() {
      Customer customer = validCustomer();
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
      Customer customer = validCustomer();
      customer.setName(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void throwsForNullEmail() {
      Customer customer = validCustomer();
      customer.setEmail(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void throwsForNullAddressCity() {
      Customer customer = validCustomer();
      customer.getAddress().setCity(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

}