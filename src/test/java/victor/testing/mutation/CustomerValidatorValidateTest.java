package victor.testing.mutation;

import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// one class to test 1 public method±
public class CustomerValidatorValidateTest {
  CustomerValidator validator = new CustomerValidator();
  Customer customer = validCustomer();

  public CustomerValidatorValidateTest() {
    System.out.println("NEW");
  }

  // canonical object
  static @NonNull Customer validCustomer() {
    Customer aCustomer = new Customer();
    aCustomer.setName("::name::");
    aCustomer.setEmail("::email::");
    aCustomer.setAddress(new Address());
    aCustomer.getAddress().setCity("::city::");
    return aCustomer;
  }

//  @BeforeEach
//  final void before() {
//    aCustomer = validCustomer();
//  }
  @Test
  void valid() {
    validator.validate(customer);
  }

  @Test
//  public void testValid_nullName_throwsException() { // JUnit3.5 (RIP ~10y ago) required "test" prefix
//  void valid_nullName_throwsException() { // when->what
//  void validThrows_forNullName() { // what<-why
  void throws_forNullName() { // †ested method name -> test class name
//    Customer aCustomer = new Customer();
//    aCustomer.setEmail("::email::");
//    aCustomer.setAddress(new Address());
//    aCustomer.getAddress().setCity("::city::");
    customer.setName(null); // data tweak

    assertThrows(
        IllegalArgumentException.class,
        () -> validator.validate(customer));
  }


  @Test
  void throws_forNullEmail() {
//    Customer aCustomer = new Customer();
//    aCustomer.setName("some name");
//    aCustomer.setAddress(new Address());
//    aCustomer.getAddress().setCity("::city::");
    customer.setEmail(null);
//    var e = assertThrows(
//        IllegalArgumentException.class,
//        () -> validator.validate(aCustomer));
//    assertTrue(e.getMessage().contains("email"));
    Assertions.assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("email");
  }

  @Test
  void throws_forNullCity() {
    customer.getAddress().setCity(null);

    Assertions.assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("city");
  }

  @Test
  void throws_forShortCityName() {
    customer.getAddress().setCity("Io");

    Assertions.assertThatThrownBy(() -> validator.validate(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("city");
  }
}