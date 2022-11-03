package victor.testing.mutation;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerValidatorShould {
   private static Customer validCustomer = new Customer()
           .setName("::name::")
           .setEmail("::email::")
           .setAddress(new Address().setCity("::city::"));
   CustomerValidator validator = new CustomerValidator();

   @Test
   void validate1() {
      validator.validate(validCustomer);
   }
   @Test
   void throwsForNullName() {
      assertThatThrownBy(() ->validator.validate(validCustomer.setName(null)));
   }


   @Test
//   void whenTheCustomerNameIsNull_throw() { // given..when..then approach of naming 🥱
   @DisplayName("strange edge case you need a human readable explanation from 1y ago that no one updated")
   void validate2() { // then..when more exciting.🔥
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      // org.junit.jupiter.api.Assertions (java5) <- sucks!. never use it,
      // instead use AssertJ library
      // ❤️org.assertj.core.api.Assertions.assertThatThrownBy(org.assertj.core.api.ThrowableAssert.ThrowingCallable)
      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
//              .matches(e -> e.getCode() == MISSING_NAME) // enum alternative
              .hasMessage("Missing the customer name") // the most precise, best for ex messages aimed for developoers only.
      ;
//              .hasMessageContaining("name"); // less failures, possible false negatives
   }

}