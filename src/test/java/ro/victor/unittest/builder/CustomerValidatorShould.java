package ro.victor.unittest.builder;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CustomerValidatorShould {

	private final CustomerValidator customerValidator = new CustomerValidator();

	private Customer aValidCustomer() {
		return new Customer()
			.setName("John")
			.setAddress(aValidAddress());
	}

	private Address aValidAddress() {
		return new Address()
			.setCity("Bucuresti")
			.setStreetName("Pompei");
	}

	@Test
	public void success() {
		customerValidator.validate(aValidCustomer());
		Assert.assertEquals(1, customerValidator.getValidationCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithNullName() {
		customerValidator.validate(aValidCustomer().setName(null));
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithAddressCityNull() {
		customerValidator.validate(aValidCustomer().setAddress(
			aValidAddress().setCity(null)));
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithAddressStreetNull() {
		customerValidator.validate(aValidCustomer().setAddress(
			aValidAddress().setStreetName(null)));
	}

//	@Rule
//	ExpectedException e

	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithNullAddress() {
		customerValidator.validate(aValidCustomer().setAddress(null));
	}

}