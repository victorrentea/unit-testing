package victor.testing.mutation;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void acceptsValidCustomer() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      validator.validate(customer);
   }

//   void invalidName() {
//   void shouldThrowForMissingName() {
//   void givenInvalidName_shouldThrow() {
//   void shouldThrowGivenInvalidName() {
   @Test
   void throwsForMissingName() {
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class);
   }
   @Test
   void throwsForMissingEmail() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing customer email");
   }
}