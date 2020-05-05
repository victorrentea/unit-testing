package ro.victor.unittest.builder;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.victor.unittest.builder.MyException.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

	@Test(expected = MyException.class)
	public void throwsForBlankName() {
		validator.validate(aValidCustomer().setName(null));
	}

	@org.testng.annotations.Test
	public void throwsForNoAddress() {
		MyException myException = Assertions.catchThrowableOfType(() -> {
			validator.validate(aValidCustomer().setAddress(null));
		}, MyException.class);

		assertThat(myException.getErrorCode()).isEqualTo(ErrorCode.MISSING_CUSTOMER_ADDRESS);


		assertThatThrownBy(() -> {
			validator.validate(aValidCustomer().setAddress(null));
		}).isInstanceOf(MyException.class)/*.hasMessage(…)*/
				.hasFieldOrPropertyWithValue("errorCode",ErrorCode.MISSING_CUSTOMER_ADDRESS);

	}

	@Rule
	public ExpectedException expectedException = none();

	@Test
	public void throwsForNoAddressCity() {
		expectedException.expectMessage("City");
		validator.validate(aValidCustomer()
				.setAddress(new Address().setCity(null)));
	}

}