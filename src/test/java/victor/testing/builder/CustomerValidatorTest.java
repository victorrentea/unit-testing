package victor.testing.builder;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static victor.testing.builder.MyException.ErrorCode.CUSTOMER_MISSING_CITY;


class OtherClassTest {
   @Test
   void test() {
      Customer customer = TestData.aValidCustomer();
   }
}

public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();
   private Customer customer = TestData.aValidCustomer();

   @Test
   void valid() {
      validator.validate(customer);
   }
   @Test
   void throwsForNullName() {
      customer.setName(null);

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("name")
          ;
   }
   @Test
   void throwsForNullEmail() {
      customer.setEmail(null);

      assertThatThrownBy(() -> validator.validate(customer))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("email")
          ;
   }
   @Test
   void throwsForNullCity() {
      customer.getAddress().setCity(null);

      assertThatThrownBy(() -> validator.validate(customer))
//          .isInstanceOf(IllegalArgumentException.class)
//          .hasMessageContaining("Missing address city")
          .matches(MyException.hasCode(CUSTOMER_MISSING_CITY))
          ;
   }

//   @Test
//   void NEVER_MOCK_DATA_OBJECTS() {
//      Address addressMock = Mockito.mock(Address.class); // NEVER; terrile practice
//      customer.setAddress(addressMock);
//      Mockito.when(addressMock.getCity()).thenReturn(" ab ");
//
//      validator.validate(customer);
//
//      Mockito.verify(addressMock).setCity("ab");
//   }
   @Test
   void correctWayToTestSideEffectsOnInMemoryState() {
      customer.getAddress().setCity(" ab ");

      validator.validate(customer);

      assertThat(customer.getAddress().getCity()).isEqualTo("ab");
   }



}