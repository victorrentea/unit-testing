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

	@Test
	public void ok() {
		validator.validate(DummyDataForCustomerSubdomain.aValidCustomer());
	}


	@Test(expected = MyException.class)
	public void throwsForBlankName() {
		Customer customer = DummyDataForCustomerSubdomain.aValidCustomer().setName(null);

		validator.validate(customer);
	}

	@org.testng.annotations.Test
	public void throwsForNoAddress() {
		// fixture
		Customer customer = DummyDataForCustomerSubdomain.aValidCustomer().setAddress(null);

		// act
		MyException myException = Assertions.catchThrowableOfType(
				() -> validator.validate(customer), MyException.class);

		// assert
		assertThat(myException.getErrorCode()).isEqualTo(ErrorCode.MISSING_CUSTOMER_ADDRESS);


		assertThatThrownBy(() -> {
			validator.validate(customer);
		}).isInstanceOf(MyException.class)/*.hasMessage(…)*/
				.hasFieldOrPropertyWithValue("errorCode",ErrorCode.MISSING_CUSTOMER_ADDRESS);

	}

	@Rule
	public ExpectedException expectedException = none();

	@Test
	public void throwsForNoAddressCity() {
		expectedException.expectMessage("City");
		validator.validate(DummyDataForCustomerSubdomain.aValidCustomer()
				.setAddress(new Address().setCity(null)));
	}

}