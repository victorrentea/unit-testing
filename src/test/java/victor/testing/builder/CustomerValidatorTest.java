package victor.testing.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CustomerValidatorTest {

   private CustomerValidator validator = new CustomerValidator();

//   public void whenCustomerNameIsEmpty_throws() {
//   public void emptyName() { // cam lene
   @Test(expected = IllegalArgumentException.class)
   public void throwsForMissingAddress() {
      Customer customer = new Customer();
      customer.setName("John");
      validator.validate(customer);
   }

   @Test(expected = IllegalArgumentException.class)
   public void throwsForEmptyName() {
      validator.validate(new Customer());
   }

   @Rule
   public ExpectedException exception = ExpectedException.none();

   @Test
   public void throwsForEmptyCity() {

      Customer customer = new Customer();
      customer.setName("John");
      Address address = new Address();
      customer.setAddress(address);

//      exception.expectMessage("city");// Draga JUnit, vezi ca testul asta o sa crape cu exceptie. E Ok. relax. Cata vreme ex cotnine "city"
//      validator.validate(customer);
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> validator.validate(customer));

      assertTrue(ex.getMessage().contains("city"));
   }
}