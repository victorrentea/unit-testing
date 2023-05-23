package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


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

      Assertions.assertEquals(customer.getAddress().getCity(), "123");
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

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void throwsForMissingEmail() {
      customer.setEmail(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void throwsForMissingAddressCity() {
      customer.getAddress().setCity(null);

      IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class,
          () -> validator.validate(customer));
      Assertions.assertEquals("Missing address city", e.getMessage());
   }

   @Test
   void throwsForAddressCityTooShort() {
      customer.getAddress().setCity("12");

      IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class,
          () -> validator.validate(customer));
      Assertions.assertEquals("Address city too short", e.getMessage());
   }


}