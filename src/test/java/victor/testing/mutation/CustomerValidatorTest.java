package victor.testing.mutation;


import org.junit.Assert;
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
       // givenAAcustomer_whenHerNameIsNull_thenThrowsException
//   void invalidCustomerName_throwsException() {
//   void throwsExceptionWhenInvalidCustomerName() {
//   void throwsWhenInvalidCustomerName() {
//   void throwsForInvalidCustomerName() {
//   void throwsForInvalidName() {
   void throwsForNullName() {
      customer.setName(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void throwsForNullEmail() {
      customer.setEmail(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void throwsForNullAddressCity() {
      customer.getAddress().setCity(null);

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

}