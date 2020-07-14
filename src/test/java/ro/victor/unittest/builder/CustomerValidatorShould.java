package ro.victor.unittest.builder;

import org.junit.Test;

public class CustomerValidatorShould {


	private final CustomerValidator customerValidator = new CustomerValidator();

	private Customer aValidCustomer() {
		Customer c = new Customer()
			.setName("John")
			.setAddress(new Address());

		return new CustomerBuilder()
			.withName("Gigel")
			.withAddress(new AddressBuilder()
				.withCity("Bucuresti")
				.build())
			.build();
	}

	@Test
	public void yesSir() {
		Customer customer = aValidCustomer();
		customerValidator.validate(customer);
	}

	@Test (expected = IllegalArgumentException.class)
	public void throwForCustomerWithNullName() {
		Customer customer = aValidCustomer();
		customer.setName(null);
		customerValidator.validate(customer);
	}

	@Test (expected = IllegalArgumentException.class)
	public void throwForCustomerWithNullAddress() {
		Customer customer = aValidCustomer();
		customer.setAddress(null);
		customerValidator.validate(customer);
	}

}