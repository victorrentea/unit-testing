package victor.testing.builder;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import victor.testing.builder.MyException.ErrorCode;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {
		validator.validate(aValidCustomer());
//		customer.getLabel===null
	}

	private Customer aValidCustomer() {
		return new Customer()
				.setName("John")
				.setAddress(aValidAddress());
	}

	private Address aValidAddress() {
		return new Address()
				.setCity("Bucuresti, Orasul Smogului");
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		validator.validate(aValidCustomer().setName(null));
	}

	@Test
	public void throwsForNullAddress() {
		Customer customer = aValidCustomer().setAddress(null);
		assertThatThrownBy(() -> validator.validate(customer))
				.matches(e -> ((MyException) e).getCode() == ErrorCode.NO_ADDRESS);
	}

	@Test
	public void whenCustomerHasAddressWithNullCity_thenThrows() {
//	public void throwsForAddressWithNullCity() {
		Customer customer = aValidCustomer().setAddress(
				aValidAddress().setCity(null));
		
		assertThatExceptionOfType(MyException.class)
			.isThrownBy(() ->validator.validate(customer))
			.matches(e -> e.getCode() == ErrorCode.NO_CITY);
		
		assertThatThrownBy(() -> validator.validate(customer))
				.matches(e -> ((MyException) e).getCode() == ErrorCode.NO_CITY);
	}

}