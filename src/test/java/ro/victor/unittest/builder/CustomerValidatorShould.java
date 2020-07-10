package ro.victor.unittest.builder;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.victor.unittest.MyException;
import ro.victor.unittest.MyException.ErrorCode;
import ro.victor.unittest.tricks.MyExceptionMatcher;

class ObjectMother {

	public static Customer aValidCustomer() {
		return new Customer()
			.setName("nume")
			.setPhone("123123213")
			.setAddress(aValidAddress());
	}

	public static Address aValidAddress() {
		return new Address()
			.setCity("Bucharest");
	}
}

class AltTest {
	@Test
	public void test() {
		Customer customer = ObjectMother.aValidCustomer();
	}
}
public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

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
		validator.validate(ObjectMother.aValidCustomer().setName(null));
	}

	@Test
	public void throwsForNullAddress() {
		Assert.assertEquals(0, x);
		expectedException.expect(new MyExceptionMatcher(ErrorCode.MISSING_CUSTOMER_ADDRESS));
		validator.validate(ObjectMother.aValidCustomer().setAddress(null));
	}

	@Test
	public void ok() {
		validator.validate(ObjectMother.aValidCustomer());
	}

	@Test
	public void throwsForNoCity() {
//		MyException exception = Assertions.assertThatExceptionOfType()() ->
		MyException exception = Assertions.catchThrowableOfType(() ->
			validator.validate(ObjectMother.aValidCustomer().setAddress(ObjectMother.aValidAddress().setCity(null))), MyException.class);
		Assertions.assertThat(exception.getCode()).isEqualTo(ErrorCode.MISSING_CUSTOMER_ADDRESS_CITY);
	}

}