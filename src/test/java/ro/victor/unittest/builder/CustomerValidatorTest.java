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
		return new Customer()
			.setName("John")
			.setPhone("phone")
			.setAddress(aValidAddress());
	}

	private Address aValidAddress() {
		return new Address()
			.setCity("Iasi")
			.setStreetName("Stefan")
			.setStreetNumber(19);
	}

	@Test(expected = IllegalArgumentException.class)
//	public void test2() {
//	public void whenCustomerHasNoName_thenValidationFails() {
	public void throwsForCustomerWithNoName() {
		validator.validate(aValidCustomer().setName(null));
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithNoPhone() {
		validator.validate(aValidCustomer().setPhone(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithAddressWithNoCity() {
		validator.validate(aValidCustomer().setAddress(
			aValidAddress()
				.setCity(null)));
	}

}