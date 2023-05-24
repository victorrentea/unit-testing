package victor.testing.mutation;


import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();
   Customer customer;

   private static Customer validCustomer() { // factory method for test data
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      return customer;
   }
   @BeforeEach // run code before every @Test
   final void fixture() {
      customer = validCustomer();
   }

   @Test
   void acceptsValidCustomer() {
      validator.validate(customer);
   }

   //   void invalidName() {
//   void shouldThrowForMissingName() {
//   void givenInvalidName_shouldThrow() {
//   void shouldThrowGivenInvalidName() {
   @Test
   void throwsForMissingName() {
      customer.setName(null);

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class);
   }

   @Test
   void throwsForMissingEmail() {
      customer.setEmail(null);

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing customer email");
   }

   @Test
   void throwsForMissingAddressCity() {
      customer.getAddress().setCity(null);

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing address city");
   }

}