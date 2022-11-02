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
      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("Missing customer name");
//              .hasMessageContaining("name");
   }

}