package victor.testing.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static victor.testing.builder.ObjectMother.aValidAddress;
import static victor.testing.builder.ObjectMother.aValidCustomer;

public class CustomerValidatorTest {

   @Rule
   public ExpectedException exception = ExpectedException.none();

   private CustomerValidator validator = new CustomerValidator();

   //   public void whenCustomerNameIsEmpty_throws() {
//   public void emptyName() { // cam lene
   @Test(expected = IllegalArgumentException.class)
   public void throwsForMissingAddress() {
      validator.validate(aValidCustomer().setAddress(null));
   }

   @Test(expected = IllegalArgumentException.class)
   public void throwsForEmptyName() {
      validator.validate(aValidCustomer().setName(null));
   }

   @Test
   public void throwsForEmptyCity() {

      Customer customer = aValidCustomer()
         .setAddress(aValidAddress().setCity(null));

//      exception.expectMessage("city");// Draga JUnit, vezi ca testul asta o sa crape cu exceptie. E Ok. relax. Cata vreme ex cotnine "city"
//      validator.validate(customer);
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> validator.validate(customer));

      assertTrue(ex.getMessage().contains("city"));
   }

   @Test
   public void valid() {
      validator.validate(ObjectMother.aValidCustomer());
   }

}
// si alte teste vor vrea metodele astea. Atentie, daca pre amulti vor, Copy-Paste clasa la un moment dat (100+ teste)
class ObjectMother {
   public static Customer aValidCustomer() {
      return new Customer()
          .setName("John")
          .setAddress(aValidAddress());
   }

   public static Address aValidAddress() {
      return new Address()
          .setCity("Oras")
          .setStreetName("Dristor")
          .setStreetNumber(12);
   }

}