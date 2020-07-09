package ro.victor.unittest.builder;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.victor.unittest.MyException;
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


	static int x = 0; // morala: ceea ce pui pe campuri in clasa de test se
	// reseteaza oricum pt unrmatorul @Test,
	// ca se reinstantiaza clasa asta pt fiecare @test

	{
		System.out.println("Ma nasc");
	}

	@Test
	public void throwsForNullName() {
		x = 1;
		Assert.assertEquals(1, x);
		expectedException.expect(new MyExceptionMatcher(ErrorCode.MISSING_CUSTOMER_NAME));
		validator.validate(aValidCustomer().setName(null));
	}

	@Test
	public void throwsForNullAddress() {
		Assert.assertEquals(0, x);
		expectedException.expect(new MyExceptionMatcher(ErrorCode.MISSING_CUSTOMER_ADDRESS));
		validator.validate(aValidCustomer().setAddress(null));
	}

	@Test
	public void ok() {
		validator.validate(aValidCustomer());
	}

	@Test
	public void throwsForNoCity() {
//		MyException exception = Assertions.assertThatExceptionOfType()() ->
		MyException exception = Assertions.catchThrowableOfType(() ->
			validator.validate(aValidCustomer().setAddress(aValidAddress().setCity(null))), MyException.class);
		Assertions.assertThat(exception.getCode()).isEqualTo(ErrorCode.MISSING_CUSTOMER_ADDRESS_CITY);
	}

}