package victor.testing.mutation;


// junit 4
//import org.junit.Test;
import org.junit.jupiter.api.Test;

        import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static victor.testing.mutation.TestData.aCustomer;

public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();

   // Junit 5 exceptiile se fac cu assertThrows -> BEST: assertThatThrownBy( -> ).isInstanceOf...
   // JUnit 4 metodele @Test publice
   // JUnit 5 nu mai are @Rule ci @RegisterExtension
   @Test
   public void valid() {
      Customer customer = aCustomer();
      customer.getAddress().setCity(" gggg ");
      validator.validate(customer);
      assertThat(customer.getAddress().getCity())
              .isEqualTo("gggg");
   }

   @Test///(expected = IllegalArgumentException.class)
//   public void whenCustomerNameNull_throws() { //A
   public void throwsForMissingCustomerName() { //B❤️
//      Customer customer = new Customer();
//      customer.setEmail("::email::");
//      customer.setAddress(new Address());
//      customer.getAddress().setCity("::city::");
      Customer customer = aCustomer().setName(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Missing customer name")
              .isInstanceOf(IllegalArgumentException.class);
//      IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class,
//              () -> validator.validate(customer));
//      Assert.assertEquals("Missing customer name", e.getMessage());
   }
   @Test//(expected = IllegalArgumentException.class)
   public void throwsForMissingCustomerEmail() { //B❤️
      Customer customer = aCustomer().setEmail(null);
//      IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class,
//              () -> validator.validate(customer));
//      Assert.assertEquals("Missing customer email", e.getMessage());
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Missing customer email")
              .isInstanceOf(IllegalArgumentException.class);
   }

   @Test
   public void throwsForNullCity() {
      Customer customer = aCustomer().setAddress(TestData.anAddress().setCity(null));
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Missing address city")
              .isInstanceOf(IllegalArgumentException.class);
   }
   @Test
   public void throwsForCityTooShort() {
      Customer customer = aCustomer().setAddress(TestData.anAddress().setCity("Go"));
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Address city too short")
              .isInstanceOf(IllegalArgumentException.class);
   }

}