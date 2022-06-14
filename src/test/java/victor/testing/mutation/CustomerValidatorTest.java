package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.bouncycastle.operator.InputAEADDecryptor;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void valid() {
      // Arrange / Given
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      // Act / When
      validator.validate(customer);
   }


   @Test
   void throwsWhenCityNameIsTooShort() {
      // Arrange / Given
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("12");

      // Act / When
      // conceptual asta e ideea, doar ca sunt frameworkuri care fac asta mult mai usor+safe (vezi mai jos)
//      try {
//         validator.validate(customer);
//         Assert.fail("Trebuia s-arunce");
//      } catch (IllegalArgumentException e) {
//         // ce fac aici ? Sunt intr-un tst
////         Assertions.assertTrue(true); //
//      }

      // JUnit 5 style
      Assertions.assertThrows(IllegalArgumentException.class,
              () -> validator.validate(customer) );

      // nu ar trebui sa folositi Assertions din jupiter, ci Assertions din assertj, ca de ex:
      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessageContaining("city too short");

   }

}