package victor.testing.mutation;


import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tools.HumanReadableTestNames;

@DisplayNameGeneration(HumanReadableTestNames.class)
//public class CustomerValidatorShould { // tends to scare devs out because they search CVT
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

  @Test
//  @DisplayName("if a customer with a null name is validated, it throw an exception")
  // "//comments are bad if you CAN refactor the code to be self-describing" <- Clean Code
  // they can become outdated. out of sync with the test

//   void notValid_ifNameIsNull() {
//  void invalidWhenNameIsNull() {
//  void givenACustoemrWithNullName_validatioFails() {
  void throwForNullName() { // naming convention to start with the EFFECT expected
    Customer customer = new Customer();
    customer.setEmail("::email::");
    customer.setAddress(new Address());
    customer.getAddress().setCity("::city::");

    validator.validate(customer);
  }


  // what if customer.address is null
}