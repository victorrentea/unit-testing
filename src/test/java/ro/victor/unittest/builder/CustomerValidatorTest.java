package ro.victor.unittest.builder;

import org.junit.Test;

public class CustomerValidatorTest {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {
		Customer customer = aValidCustomer();

		validator.validate(customer);
	}

	// dummy data for test
	private Customer aValidCustomer() {
		Customer customer = new Customer();
		customer.setName("John");
		customer.setPhone("phone");
		Address address = new Address();
		address.setCity("Iasi");
		customer.setAddress(address);
		return customer;
	}

	@Test(expected = IllegalArgumentException.class)
//	public void test2() {
//	public void whenCustomerHasNoName_thenValidationFails() {
	public void throwsForCustomerWithNoName() {
		Customer customer = aValidCustomer();
		customer.setName(null);
		validator.validate(customer);
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithNoPhone() {
		Customer customer = aValidCustomer();
		customer.setPhone(null);
		validator.validate(customer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithAddressWithNoCity() {
		Customer customer = aValidCustomer();
		customer.getAddress().setCity(null);
		validator.validate(customer);
	}

}