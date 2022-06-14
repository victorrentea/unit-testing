package victor.testing.mutation;


import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.bouncycastle.operator.InputAEADDecryptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();
   private Customer customer;

   // test data factory methods
   private Customer aValidCustomer() {
      return new Customer()
              .setName("::name::")
              .setEmail("::email::")
              .setAddress(new Address()
                      .setCity("::city::"));
   }

   @BeforeEach
   final void before() {
      System.out.println("#sieu");
      customer = aValidCustomer();
   }

   @Test
   void valid() {
      // Arrange / Given
      customer.getAddress().setCity("  ::city::");

      // Act / When
      validator.validate(customer);

      // Assert / Then
//      Assertions.assertEquals("::city::", customer.getAddress().getCity()); // JUnit 5, mai bun e urmatorul:
      assertThat(customer.getAddress().getCity()).isEqualTo("::city::");
   }

   @Test
   void throwsWhenCityNameIsTooShort() {
      // Arrange / Given
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
      customer.setName(null);

      // Act / When
      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessageContaining("Missing customer name");

   }

}