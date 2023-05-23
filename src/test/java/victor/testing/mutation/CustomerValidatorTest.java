package victor.testing.mutation;


import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   Customer customer = TestData.validCustomer();

   public CustomerValidatorTest() {
      System.out.println("Cate instante de clasa de test se fac pt 4 @Test"); // cate @Test atatea instanta
      // => poti sa lasi gunoi pe campurile clasei, urmatorul test are alta instanta de clasa de test.
   }
//   @BeforeEach
//   final void before() {
//       customer = validCustomer();
//   }

   @Test
   void valid() {
      validator.validate(customer);
   }

   @Test
   void trimsAddressCity() {
      customer.getAddress().setCity(" 123 ");

      validator.validate(customer);

      assertEquals("123", customer.getAddress().getCity());
   }

   @Test
       // givenAAcustomer_whenHerNameIsMissing_thenThrowsException
//   void invalidCustomerName_throwsException() {
//   void throwsExceptionWhenInvalidCustomerName() {
//   void throwsWhenInvalidCustomerName() {
//   void throwsForInvalidCustomerName() {
//   void throwsForInvalidName() {
   void throwsForMissingName() {
      customer.setName(null);

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class);
   }

   @Test
   void throwsForMissingEmail() {
      customer.setEmail(null);

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class);
   }

   @Test
   void throwsForMissingAddressCity() {
      customer.getAddress().setCity(null);

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Missing address city");
   }

   @Test
   void throwsForAddressCityTooShort() {
      customer.getAddress().setCity("12");

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Address city too short");
   }


   @Test
   void whyAssertJ_rocks_andJupiterAssertions_sucks() { // aka assertThat rocks!!
      List<Integer> actual = prodCode();
//      assertEquals(3, actual.size());// failure message sucks
      //
      assertThat(actual)
//          .extracting(::getId)
          .contains(3)
          .containsExactlyInAnyOrder(1,2,3)
          .hasSize(3);
   }

   @NotNull
   private static List<Integer> prodCode() {
      return List.of(1, 2, 3, 2);
   }


}