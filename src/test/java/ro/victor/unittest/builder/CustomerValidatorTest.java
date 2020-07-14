package ro.victor.unittest.builder;

import org.junit.Test;

public class CustomerValidatorTest {


	private final CustomerValidator customerValidator = new CustomerValidator();

	private Customer aValidCustomer() {
		return new Customer()
			.setName("John")
//			.addLabels("1","2")
			.setAddress(aValidAddress());
	}

	private Address aValidAddress() {
		return new Address()
			.setCity("Bucuresti");
	}

	@Test
	public void yesSir() {
		customerValidator.validate(aValidCustomer());
	}

	@Test (expected = IllegalArgumentException.class)
	public void throwForCustomerWithNullName() {
		Customer customer = aValidCustomer().setName(null);
		customerValidator.validate(customer);
	}

	@Test (expected = IllegalArgumentException.class)
	public void throwForCustomerWithNullAddress() {
		Customer customer = aValidCustomer().setAddress(null);
		customerValidator.validate(customer);
	}
	@Test (expected = IllegalArgumentException.class)
	public void throwForCustomerWithNullAddressCity() {
		Customer customer = aValidCustomer().setAddress(aValidAddress().setCity(null));
		customerValidator.validate(customer);
	}

}