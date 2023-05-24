package victor.testing.mutation;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static victor.testing.mutation.TestData.aCustomer;

public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();
   Customer customer = aCustomer();
   public CustomerValidatorTest() {
      System.out.println("NEW INSTANCE");
   }
//   @BeforeEach // run code before every @Test
//   final void fixture() {
//      customer = aCustomer();
//   }
   @Test
   void acceptsValidCustomer() {
      validator.validate(customer);
   }
   @Test
   void trimsCityName() {
      customer.getAddress().setCity(" city ");

      validator.validate(customer);

      assertThat(customer.getAddress().getCity()).isEqualTo("city");
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
   @Test
   void throwsForAddressCityTooShort() {
      customer.getAddress().setCity("12");

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Address city too short");
   }

}