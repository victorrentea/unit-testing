package victor.testing.mutation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tools.HumanReadableTestNames;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(HumanReadableTestNames.class) // extensie custom
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
//   @DisplayName("Un customer care vine cu numele null va face validatorul sa arunce o IllegalArgumentException cu mesajul .... bla bla")
   // human readable.
   // RISK: desync cu ce face testul/ numele testului
   // PRO: poti explica cazuri mai complexe e bun pt teste de integrare/mai ample
   //   unde desi ai incercat nu ai putut face CORPUL sau NUMELE testului self -explanatory
//   void testCustomerNameNull() {
//   void shouldThrowException_whenCustomerNameIsNull() {
   // mai jos: nume la fel de semnificative dar mai scurte
//   void shouldThrow_whenCustomerNameIsNull() {
//   void throws_whenCustomerNameIsNull() {
   @Test//(expected = /junit4)
   void throwsForNullName() { // scurt dar la fel de semnificat
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      assertThatThrownBy(() -> validator.validate(customer)) // AssertJ ❤️ Doar AssertJ. Forever.
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing customer name"); // fluent assertions
   }
   @Test
   void throwsForNullEmail() {
      Customer customer = new Customer();
      customer.setName(":::name:::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");
      // mai jos sunt asertii facute cu org.junit (JUNIT4) sau org.jupiter (JUNIT5)
      // DE EVITAT
//      IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, // NU FOLOSI
//          () -> validator.validate(customer));
//      assertEquals("Missing customer email", e.getMessage()); // NU FOLOSI
      assertThatThrownBy(() -> validator.validate(customer)) // AssertJ ❤️ Doar AssertJ. Forever.
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing customer email"); // fluent assertions
   }

}