package victor.testing.mutation;


import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void happy() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      validator.validate(customer);
   }
//   @Rule  ExpectedException expectedException = ExpectedException.none(); / Junit 4
//   @org.junit.Test(expected = IllegalArgumentException.class) // Junit4
   @Test
//   void whenCustomerNameNull_thenExceptionIsThrown() {
   void throws_forMissingCustomerName() {
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      // JUnit5 < E NASPA
      Assertions.assertThrows(IllegalArgumentException.class,
              () -> validator.validate(customer)
      );
      // AssertJ << ASTA E BUN
      assertThatThrownBy(() -> validator.validate(customer)).isInstanceOf(IllegalArgumentException.class);
   }
   @Test
   void throws_forMissingCustomerEmail() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class);
   }



}