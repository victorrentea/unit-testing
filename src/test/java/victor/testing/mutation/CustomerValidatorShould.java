package victor.testing.mutation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void valid() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      validator.validate(customer);
   }
   @Test
//   void whenTheCustomerNameIsNull_throw() { // given..when..then approach of naming 🥱
   void throwsWhenCustomerNameIsNull() { // then..when more exciting.🔥
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