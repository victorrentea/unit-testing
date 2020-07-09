package ro.victor.unittest.builder;

import org.junit.Test;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		validator.validate(new Customer());
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddress() {
		Customer customer = new Customer()
			.setName("nume");
		validator.validate(customer);
	}

	@Test
	public void ok() {
		Customer customer = new Customer()
			.setName("nume")
			.setAddress(new Address()
				.setCity("Bucharest"));
		validator.validate(customer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNoCity() {
		Customer customer = new Customer()
			.setName("de ce eu")
			.setAddress(new Address());
		validator.validate(customer);
	}

}