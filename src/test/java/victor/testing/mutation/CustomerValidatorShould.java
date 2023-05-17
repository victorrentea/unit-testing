package victor.testing.mutation;


import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();

   // este comuna aceasta variabla tuturor testelor ?
   private Customer customer = aCustomer();

   public CustomerValidatorShould() {
      System.out.println("De cate ori se instantiaza clasa de test: " +
                         "o data pentru fiecare @Test");
      // morala: orice lasi pe campurile de instanta ale clasei de test
      // dispare pt urmatorul @Test
   }

   private static Customer aCustomer() { // generator de date de test
      // sau il citesti din JSON
      return new Customer()
          .setName("::name::")
          .setEmail("::email::")
          .setAddress(new Address()
              .setCity("::city::"));
   }

   @Test
   void valid() {
      validator.validate(customer);
   }
//   void givenCustomer_whenValidated_addressCityIsTrimmed() {
//   void checkCityAddressIsTrimmed() {
//   void checkAddressCityIsTrimmed() {
//   void addressCityIsTrimmed() {
//   @Test
//   void trimsAddressCity() {
//      customer.getAddress().setCity("  Bucharest  ");
//      validator.validate(customer);
//      assertThat(customer.getAddress().getCity()).isEqualTo("Bucharest");
//   }
   @Test
   void throwForMissingName() {
     customer.setName(null);
      assertThatThrownBy(()->validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("name");
   }
   @Test
   void throwForMissingEmail() {
      customer.setEmail(null);
      assertThatThrownBy(()->validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("email");
   }
   @Test
   void throwForMissingAddressCity() {
      customer.getAddress().setCity(null);
      assertThatThrownBy(()->validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("city");
   }
   @Test
   void throwForAddressCityTooShort() {
      customer.getAddress().setCity("aa");
      assertThatThrownBy(()->validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Address city too short");
   }

}