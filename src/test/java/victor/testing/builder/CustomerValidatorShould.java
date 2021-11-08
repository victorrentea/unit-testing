package victor.testing.builder;


import org.junit.jupiter.api.Test;
import victor.testing.builder.MyException.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static victor.testing.builder.TestData.aValidCustomer;


public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void valid() {
      Customer customer = aValidCustomer();

      validator.validate(customer);
   }

   @Test
   void trimsCityName() {
      Customer customer = aValidCustomer();
      customer.getAddress().setCity(" Orass  ");
//      customer.getAddress().setCity(" " + customer.getAddress().getCity() + " ");

      validator.validate(customer);

      assertThat(customer.getAddress().getCity()).isEqualTo("Orass");
//      assertThat(customer.getAddress().getCity()).isEqualTo(customer.getAddress().getCity());
   }

   @Test
   void throwsForNullName() {
      Customer customer = aValidCustomer().setName(null);

      assertThrows(IllegalArgumentException.class,
          () -> validator.validate(customer));
   }

   @Test
   void throwsForNullEmail() {
      Customer customer = aValidCustomer().setEmail(null);

//      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
//          () -> validator.validate(customer));
//      Assertions.assertThat(ex.getMessage()).contains("email");

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("email"); // foarte ok
   }

   @Test
   void throwsForNullAddressCity() {
      Customer customer = aValidCustomer();
      customer.getAddress().setCity(null);

      assertThrows(NoAddressCity.class,
          () -> validator.validate(customer));
   }

   @Test
   void throwsForNullCityNameTooShort() {
      Customer customer = aValidCustomer();
      customer.getAddress().setCity("Ipif");

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(MyException.class)
          .matches(e -> ((MyException)e).getCode() == ErrorCode.CUSTOMER_CITY_TOO_SHORT);
   }

   @Test
   void okCityName() {
      Customer customer = aValidCustomer();
      customer.getAddress().setCity("xyztf");

      validator.validate(customer);
   }

}