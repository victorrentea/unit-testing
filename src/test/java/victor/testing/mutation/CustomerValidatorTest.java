package victor.testing.mutation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestData { // asta se numeste acum "Object Mother" design pattern
//   https://martinfowler.com/bliki/ObjectMother.html

   // test data factory methods
   public static Customer johnCustomer() { // bucata asta e tare apetisanta pentru multe alte clase de test.
      // PERICOL: too high coupling: foarte multe teste vor depinde de aceasta metoda
      // daca cineva o modifica > BUM
      return new Customer()
              .setName("::name::")
              .setEmail("::email::")
              .setAddress(new Address()
                      .setCity("::city::"));
   }

}


public class CustomerValidatorTest extends TestBase{
   CustomerValidator validator = new CustomerValidator();
   private Customer customer= TestData.johnCustomer();


   public CustomerValidatorTest() {
      System.out.println("Fiecare @Test are parte de o instanta proaspata de clasa de test. de ce pasa: orice date varza lasi pe campurile clasei de test, se pierd dupa @Test ul tau.");
   }

   @BeforeEach
   final void before() {
      System.out.println("#sieu");
//      customer = aValidCustomer();
   }

   @Test
   void valid() {
      // Arrange / Given
      Customer customer = TestData.johnCustomer();
      customer.getAddress().setCity("  ::city::");

      // Act / When
      validator.validate(customer);

      // Assert / Then
//      Assertions.assertEquals("::city::", customer.getAddress().getCity()); // JUnit 5, mai bun e urmatorul:
      assertThat(customer.getAddress().getCity()).isEqualTo("::city::");
   }

   @Test
   void throwsWhenCityNameIsMissing() {
       customer.getAddress().setCity(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessageContaining("Missing address city");

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
   @Test
   void throwsWhenCustomerEmailIsNull() {
      // Arrange / Given
      customer.setEmail(null);

      // Act / When
      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessageContaining("customer email");

   }

}