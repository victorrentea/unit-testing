package victor.testing.builder;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import victor.testing.builder.MyException.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	private Customer customer = TestData.aCustomer();

	@Test
	public void yesSir() {

		validator.validate(customer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		validator.validate(customer.setName(null));
	}


	@Test
	public void throwsForNullCity() {
		expectedException.expect(MyException.class);
//		expectedException.expectMessage("address city");
		customer.getAddress().setCity(null);

		validator.validate(customer);
	}

	@Test
	public void throwsForNullCityNewAgeStyle() {
		customer.getAddress().setCity(null);

		MyException ex = assertThrows(
			MyException.class,
			() -> validator.validate(customer));

		assertThat(ex.getCode()).isEqualTo(ErrorCode.INVALID_CUSTOMER_ADDRESS_CITY);
	}

	@Test
	public void throwsForNullAddress() {

		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> validator.validate(customer.setAddress(null)));

		assertThat(ex.getMessage()).contains("Missing customer address");
	}



}