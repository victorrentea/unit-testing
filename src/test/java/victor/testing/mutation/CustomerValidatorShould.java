package victor.testing.mutation;


// junit 4
//import org.junit.Test;
import org.junit.jupiter.api.Test;

        import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static victor.testing.mutation.TestData.aCustomer;

public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();
   private Customer customer = aCustomer();

   // Junit 5 exceptiile se fac cu assertThrows -> BEST: assertThatThrownBy( -> ).isInstanceOf...
   // JUnit 4 metodele @Test publice
   // JUnit 5 nu mai are @Rule ci @RegisterExtension
   @Test
   public void acceptsValidCustomer() {
      validator.validate(customer);
   }
   @Test
   public void trimsCityName() {
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
      customer.setName(null);

      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Missing customer name")
              .isInstanceOf(IllegalArgumentException.class);
//      IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class,
//              () -> validator.validate(customer));
//      Assert.assertEquals("Missing customer name", e.getMessage());
   }
   @Test//(expected = IllegalArgumentException.class)
   public void throwsForMissingCustomerEmail() { //B❤️
      customer.setEmail(null);
//      IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class,
//              () -> validator.validate(customer));
//      Assert.assertEquals("Missing customer email", e.getMessage());
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Missing customer email")
              .isInstanceOf(IllegalArgumentException.class);
   }

   @Test
   public void throwsForNullCity() {
      customer.setAddress(TestData.anAddress().setCity(null));
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Missing address city")
              .isInstanceOf(IllegalArgumentException.class);
   }
   @Test
   public void throwsForCityTooShort() {
      customer.setAddress(TestData.anAddress().setCity("Go"));
      assertThatThrownBy(() -> validator.validate(customer))
              .hasMessage("Address city too short")
              .isInstanceOf(IllegalArgumentException.class);
   }

}