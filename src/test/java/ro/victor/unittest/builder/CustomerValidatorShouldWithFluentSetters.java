package ro.victor.unittest.builder;

import org.junit.Test;

public class CustomerValidatorShouldWithFluentSetters {

	private CustomerValidator validator = new CustomerValidator();


	@Test
	public void yesSir() {

		// builderul foloseste la creerea de date in diverse setupuri in mod fluent,
		// usor de citit de oameni
		validator.validate(aValidCustomer());
	}

	private Customer aValidCustomer() {
		return new Customer()
				.setName("nume")
				.setAddress(aValidAddress());
	}

	private Address aValidAddress() {
		return new Address()
				.setCity("City");
	}

}