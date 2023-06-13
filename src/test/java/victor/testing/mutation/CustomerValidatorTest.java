package victor.testing.mutation;


import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import victor.testing.tools.HumanReadableTestNames;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // tests order will start matter
// when they change the class state
@DisplayNameGeneration(HumanReadableTestNames.class)
public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();
   Customer customer = validCustomer();

//   @BeforeEach
//   final void before() {
//      customer = validCustomer();
//   }
   private static Customer validCustomer() {
      System.out.println("How many times gets called? 1 per eact @Test");
      return new Customer()
          .setName("::name::")
          .setEmail("::email::")
          .setAddress(new Address()
              .setCity("::city::"));
   }

   @Test
   void valid() {
      validator.validate(customer);
   }

   @Test
   void trimsTheCityName() {
      customer.getAddress().setCity(" abc   ");

      validator.validate(customer);

      assertThat(customer.getAddress().getCity()).isEqualTo("abc");
   }

   // deprecated:
//   @Rule // junit 4 < 4.11 version
//   ExpectedException expectedException = ExpectedException.none();
//   void invalidWithMissingName() {
//   void shouldThrowExceptionWithEmptyName() {
//   @DisplayName("throws exception if customer name is null") <- for integration tests/larger scoped tests
   // - obsolete
   // + human readable
//   void should_throw_exception_with_empty_name() { // py naming convention
//   void shouldThrowExceptionWithEmptyName() {
   @Test
//   void givenAValidator_whenACUstomerWithANullNameIsValidated_thenThrowsException()
   void throwsForNullName() {
      customer.setName(null);
      // JUnit 5
//      IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
      // Best: AssertJ "assertThat"
      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing customer name");
   }

   @Test
   void throwsForNullEmail() {
      customer.setEmail(null);
      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing customer email");
   }
   @Test
   void throwsForNullCity() {
      customer.getAddress().setCity(null);
      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing address city");
   }
   @Test
   void throwsForTooShortCity() {
      customer.getAddress().setCity("12");
      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Address city too short");
   }


}