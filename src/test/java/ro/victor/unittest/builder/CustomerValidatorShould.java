package ro.victor.unittest.builder;

import org.junit.Test;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	private Customer aValidCustomer() {
		return new Customer()
			.setName("nume")
			.setAddress(aValidAddress());
	}

	private Address aValidAddress() {
		return new Address()
			.setCity("Bucharest");
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		validator.validate(aValidCustomer().setName(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddress() {
		validator.validate(aValidCustomer().setAddress(null));
	}

	@Test
	public void ok() {
		validator.validate(aValidCustomer());
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNoCity() {
		validator.validate(aValidCustomer().setAddress(aValidAddress().setCity(null)));
	}

}