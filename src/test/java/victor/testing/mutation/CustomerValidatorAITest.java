package victor.testing.mutation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerValidatorAITest {
  CustomerValidator validator = new CustomerValidator();

  @Test
  void missingCustomerNameThrowsException() {
    Customer aCustomer = new Customer();
    aCustomer.setEmail("::email::");
    aCustomer.setAddress(new Address());
    aCustomer.getAddress().setCity("::city::");

    assertThrows(IllegalArgumentException.class, () -> validator.validate(aCustomer), "Missing customer name");
  }

  @Test
  void missingCustomerEmailThrowsException() {
    Customer aCustomer = new Customer();
    aCustomer.setName("::name::");
    aCustomer.setAddress(new Address());
    aCustomer.getAddress().setCity("::city::");

    assertThrows(IllegalArgumentException.class, () -> validator.validate(aCustomer), "Missing customer email");
  }

  @Test
  void missingAddressCityThrowsException() {
    Customer aCustomer = new Customer();
    aCustomer.setName("::name::");
    aCustomer.setEmail("::email::");
    aCustomer.setAddress(new Address());

    assertThrows(IllegalArgumentException.class, () -> validator.validate(aCustomer), "Missing address city");
  }

  @Test
  void addressCityTooShortThrowsException() {
    Customer aCustomer = new Customer();
    aCustomer.setName("::name::");
    aCustomer.setEmail("::email::");
    aCustomer.setAddress(new Address());
    aCustomer.getAddress().setCity("NY");

    assertThrows(IllegalArgumentException.class, () -> validator.validate(aCustomer), "Address city too short");
  }

  @Test
  void validCustomerPassesValidation() {
    Customer aCustomer = new Customer();
    aCustomer.setName("::name::");
    aCustomer.setEmail("::email::");
    aCustomer.setAddress(new Address());
    aCustomer.getAddress().setCity("New York");

    validator.validate(aCustomer);
  }

}