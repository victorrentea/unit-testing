package victor.testing.builder;


import org.junit.jupiter.api.Test;



public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void valid() {
      Customer customer = TestData.aValidCustomer();
      validator.validate(customer);
   }

}


class CustomerValidatorShould2 {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void valid() {
      Customer customer = TestData.aValidCustomer();
      validator.validate(customer);
   }
   @Test
   void throwsForNullEmail() {
      Customer customer = TestData.aValidCustomer().setEmail(null);
      validator.validate(customer);
   }

}

class TestData {

   public static Customer aValidCustomer() {
      return new Customer()
          .setName("::name::")
          .setEmail("::email::")
          .setAddress(new Address()
              .setCity("::city::"));
   }
}