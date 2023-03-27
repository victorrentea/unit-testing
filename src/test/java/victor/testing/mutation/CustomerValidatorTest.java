package victor.testing.mutation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void valid() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      validator.validate(customer);
   }

   @Test
//   @Expected(exception=) JUnit4
   void throwForMissingName() {
      Customer customer = new Customer();
//      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      // in general avoid using Assertions from JUnit5, instead use AssertJ
//      Assertions.assertThrows(IllegalArgumentException.class,
//              () -> validator.validate(customer));

      Assertions.assertThatThrownBy(() -> validator.validate(customer))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("Missing customer name")
//              .hasMessageContaining("name")
      ;

   }

}