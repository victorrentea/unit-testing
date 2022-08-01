package victor.testing.mutation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestData { // Object Mother F....

   public static Customer john() {
      return new Customer()
              .setName("::name::")
              .setEmail("::email::")
              .setAddress(new Address()
                      .setCity("::city::"));
   }
}

public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();
   private Customer customer = TestData.john();

   public CustomerValidatorTest() {
      System.out.println("Pentru fiecare @Test se face o instanta noua => nu tre sa-ti pese de ce gunoi lasi in campurir");
   }

   @Test
   void valid() {
      customer.getAddress().setCity(" city  ");

      validator.validate(customer);

      assertThat(customer.getAddress().getCity())
              .isEqualTo("city");
   }

   @Test
   void validateThrows_forMissingName() {
      // given = contextul
      customer.setName(null);

      // when = call prod
      assertThatThrownBy(  () -> validator.validate(customer)  )
              .isInstanceOf(CustomerValidationException.class)
              .matches(e -> ((CustomerValidationException)e).getFieldInError().equals("name"));
   }

   @Test
   void validateThrows_forMissingEmail() {
      // given = contextul
      customer.setEmail(null);

      // when = call prod
      Assertions.assertThrows(IllegalArgumentException.class,
              () -> validator.validate(customer));
   }
   @Test
   void validateThrows_forMissingCity() {
      // given = contextul
      customer.getAddress().setCity(null);

      // when = call prod
      Assertions.assertThrows(IllegalArgumentException.class,
              () -> validator.validate(customer));
   }

   @Test
   void validateThrows_forTooShortCity() {
      // given = contextul
      customer.getAddress().setCity("Io");

      // when = call prod
      Assertions.assertThrows(IllegalArgumentException.class,
              () -> validator.validate(customer));
   }

}