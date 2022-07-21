package victor.testing.mutation;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
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

   @Test
   void happy() {
      validator.validate(customer);
   }
   @Test
   void trimsCityName() {
      customer.getAddress().setCity("  Bostonshine  ");

      validator.validate(customer);

//      assertEquals("Bostonshine", customer.getAddress().getCity());

      assertThat(customer.getAddress().getCity()) //
              .isEqualTo("Bostonshine");
   }

   @Test
   void throws_forMissingName() {
      customer.setName(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessageContaining("Missing customer name"); // todo la fel pt celalalte
   }
   @Test
   void throws_forMissingEmail() {
      customer.setEmail(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class);
   }
   @Test
   void throws_forMissingAddressCity() {
      customer.getAddress().setCity(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class);
   }
   @Test
   void throws_forAddressCityTooShort() {
      customer.getAddress().setCity("IO");

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class);
   }

}