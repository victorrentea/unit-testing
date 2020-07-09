package ro.victor.unittest.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.victor.unittest.MyException.ErrorCode;
import ro.victor.unittest.tricks.MyExceptionMatcher;

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

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void throwsForNullName() {
		expectedException.expect(new MyExceptionMatcher(ErrorCode.MISSING_CUSTOMER_NAME));
		validator.validate(aValidCustomer().setName(null));
	}

	@Test
	public void throwsForNullAddress() {
		expectedException.expect(new MyExceptionMatcher(ErrorCode.MISSING_CUSTOMER_ADDRESS));
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