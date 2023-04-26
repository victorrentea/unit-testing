package victor.testing.mutation;


// junit 4
import org.junit.Assert;
import org.junit.Rule;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();

   // Junit 5 exceptiile se fac cu assertThrows -> BEST: assertThatThrownBy( -> ).isInstanceOf...
   // JUnit 4 metodele @Test publice
   // JUnit 5 nu mai are @Rule ci @RegisterExtension
   @Test
   public void valid() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      validator.validate(customer);
   }
   @Test///(expected = IllegalArgumentException.class)
//   public void whenCustomerNameNull_throws() { //A
   public void throwsForMissingCustomerName() { //B❤️
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Missing customer name")
              .isInstanceOf(IllegalArgumentException.class);
//      IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class,
//              () -> validator.validate(customer));
//      Assert.assertEquals("Missing customer name", e.getMessage());
   }
   @Test//(expected = IllegalArgumentException.class)
   public void throwsForMissingCustomerEmail() { //B❤️
      Customer customer = new Customer();
      customer.setAddress(new Address());
      customer.setName("nu nume");
      customer.getAddress().setCity("::city::");
//      IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class,
//              () -> validator.validate(customer));
//      Assert.assertEquals("Missing customer email", e.getMessage());
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Missing customer email")
              .isInstanceOf(IllegalArgumentException.class);
   }

   @Test
   public void throwsForNullCity() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Missing address city")
              .isInstanceOf(IllegalArgumentException.class);
   }
   @Test
   public void throwsForCityTooShort() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("Go");
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Address city too short")
              .isInstanceOf(IllegalArgumentException.class);
   }

}