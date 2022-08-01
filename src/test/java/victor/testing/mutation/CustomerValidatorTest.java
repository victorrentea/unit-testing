package victor.testing.mutation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
      validator.validate(customer);
   }

   @Test
   void validateThrows_forMissingName() {
      // given = contextul
      customer.setName(null);

      // when = call prod
      Assertions.assertThrows(IllegalArgumentException.class,
              () -> validator.validate(customer));
   }

   @Test
   void validateThrows_forMissingEmail() {
      // given = contextul
      customer.setEmail(null);

      // when = call prod
      Assertions.assertThrows(IllegalArgumentException.class,
              () -> validator.validate(customer));
   }

}