package victor.testing.mutation;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

class TestData {
   // e prea tehnica.
   public static Customer john() {
      return new Customer()
              .setName("::name::")
              .setEmail("::email::")
              .setAddress(new Address()
                      .setCity("::city::"));
   } // Object Mother

}

public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   Customer customer = TestData.john(); // oare obiectul ramane in memorie INTRE TESTE?


   public CustomerValidatorTest() {
      System.out.println("Fiecare @Test are o noua instanta de clasa de test?!!");
   }
   @Test
   void happy() {
      validator.validate(customer);
   }

   @Test
   void throws_forMissingCustomerName() {
      customer.setName(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class);
   }
   @Test
   void throws_forMissingCustomerEmail() {
      customer.setEmail(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class);
   }



}