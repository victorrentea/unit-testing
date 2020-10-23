package victor.testing.builder;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import victor.testing.builder.MyException.ErrorCode;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {
		validator.validate(TestData.aValidCustomer());
//		customer.getLabel===null
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		validator.validate(TestData.aValidCustomer().setName(null));
	}

	@Test
	public void throwsForNullAddress() {
		Customer customer = TestData.aValidCustomer().setAddress(null);
		assertThatThrownBy(() -> validator.validate(customer))
				.matches(e -> ((MyException) e).getCode() == ErrorCode.NO_ADDRESS);
	}

	@Test
	public void whenCustomerHasAddressWithNullCity_thenThrows() {
//	public void throwsForAddressWithNullCity() {
		Customer customer = TestData.aValidCustomer().setAddress(
				TestData.aValidAddress().setCity(null));
		
		
		assertThatExceptionOfType(MyException.class)
			.isThrownBy(() ->validator.validate(customer))
			.matches(e -> e.getCode() == ErrorCode.NO_CITY);
		
		assertThatThrownBy(() -> validator.validate(customer))
				.matches(e -> ((MyException) e).getCode() == ErrorCode.NO_CITY);
		
		MyException myException = assertThrows(MyException.class, () -> validator.validate(customer));
		assertEquals(ErrorCode.NO_CITY, myException.getCode());
	}

}