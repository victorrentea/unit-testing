package ro.victor.unittest.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CustomerValidatorTest {


	private final CustomerValidator customerValidator = new CustomerValidator();

	public CustomerValidatorTest() {
		System.out.println("O noua instanta de clasa de TEST se naste pentru fiecare @Test in parte");
	}

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

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void throwForCustomerWithNullAddressCity() {
		expectedException.expectMessage("address city");
		Customer customer = aValidCustomer().setAddress(aValidAddress().setCity(null));
		customerValidator.validate(customer);
	}

}