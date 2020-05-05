package ro.victor.unittest.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.rules.ExpectedException.none;

public class CustomerValidatorTest {

	private CustomerValidator validator = new CustomerValidator();

	private Customer aValidCustomer() {
		return new Customer()
				.setName("John")
				.setAddress(new Address().setCity("Bucharest"));
	}

	@Test
	public void ok() {
		validator.validate(aValidCustomer());
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForBlankName() {
		validator.validate(aValidCustomer().setName(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNoAddress() {
		validator.validate(aValidCustomer().setAddress(null));
	}

	@Rule
	public ExpectedException expectedException = none();

	@Test
	public void throwsForNoAddressCity() {
		expectedException.expectMessage("city");
		validator.validate(aValidCustomer()
				.setAddress(new Address().setCity(null)));
	}

}