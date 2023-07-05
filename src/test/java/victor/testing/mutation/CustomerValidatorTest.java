package victor.testing.mutation;


import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tools.HumanReadableTestNames;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(HumanReadableTestNames.class) // extensie custom
public class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();

   // scopul unui builder: sa populezi un obiect cu un constructor fff mare si scarbos (max 5)
   Customer customer = TestData.musk();

  public CustomerValidatorTest() {
      System.out.println("De cate ori e o instanta din clasa de test creeata ?");
   }

  @Test
  void valid1() {
    validator.validate(customer);
  }
  @Test
  void trimsCityName() {
    //aici ma manca grav sa am custmer cu email a@b.com
    customer.setEmail("a@b.com") // tweak the customer state in YOUR @Test
        .getAddress().setCity(" Braila ");

    validator.validate(customer);

    assertThat(customer.getAddress().getCity()).isEqualTo("Braila");
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
  @Test
  void throwsForNullName2() { // scurt dar la fel de semnificat
    customer.setName(null); // Genu proxim si diferenta specifica

    assertThatThrownBy(() -> validator.validate(customer)) // AssertJ ❤️ Doar AssertJ. Forever.
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Missing customer name"); // fluent assertions
  }
  @Test
  void throwsForNullEmail3() {
    customer.setEmail(null);


    // mai jos sunt asertii facute cu org.junit (JUNIT4) sau org.jupiter (JUNIT5)
    // DE EVITAT
//      IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, // NU FOLOSI
//          () -> validator.validate(customer));
//      assertEquals("Missing customer email", e.getMessage()); // NU FOLOSI
    assertThatThrownBy(() -> validator.validate(customer)) // AssertJ ❤️ Doar AssertJ. Forever.
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Missing customer email"); // fluent assertions
  }
  @Test
  void throwsForNullCity3() {
     customer.getAddress().setCity(null);
    assertThatThrownBy(() -> validator.validate(customer)) // AssertJ ❤️ Doar AssertJ. Forever.
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Missing address city");
  }
  @Test
  void throwsForNullCityTooShort() {
     customer.getAddress().setCity("12");
    assertThatThrownBy(() -> validator.validate(customer)) // AssertJ ❤️ Doar AssertJ. Forever.
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Address city too short");
  }
//  @Test
//  void throwsForNullAddress() {
//     customer.setAddress(null)
//    assertThatThrownBy(() -> validator.validate(customer)) // AssertJ ❤️ Doar AssertJ. Forever.
//        .isInstanceOf(NullPointerException.class);
//  }

}