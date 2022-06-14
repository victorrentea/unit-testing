package victor.testing.mutation;


import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.bouncycastle.operator.InputAEADDecryptor;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   // test data factory methods
   private Customer aValidCustomer() {
      return new Customer()
              .setName("::name::")
              .setEmail("::email::")
              .setAddress(new Address()
                      .setCity("::city::"));
   }

   @Test
   void valid() {
      // Arrange / Given
      Customer customer = aValidCustomer();
      customer.getAddress().setCity("  ::city::");

      // Act / When
      validator.validate(customer);

      // Assert / Then
      Assertions.assertEquals("::city::", customer.getAddress().getCity());
   }



   @Test
   void throwsWhenCityNameIsTooShort() {
      // Arrange / Given
      Customer customer = aValidCustomer();
      customer.getAddress().setCity("12");

      // Act / When
      // JUnit 5 style
      Assertions.assertThrows(IllegalArgumentException.class,
              () -> validator.validate(customer) );

      // Assertions: nu ar trebui sa folositi Assertions din jupiter, ci Assertions din assertj, ca de ex:
      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessageContaining("city too short");

   }
   @Test
   void throwsWhenCustomerNameIsNull() {
      // Arrange / Given
      Customer customer = aValidCustomer();
      customer.setName(null);

      // Act / When
      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessageContaining("Missing customer name");

   }

}