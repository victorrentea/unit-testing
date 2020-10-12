package victor.testing.builder;



import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerValidatorTest {

	private CustomerValidator validator = new CustomerValidator();

	private Customer aValidCustomer() {
		return new Customer()
			.setName("John")
			.addLabel("a","b")
			.setAddress(aValidAddress());
	}

	private Address aValidAddress() {
		return new Address()
			.setCity("Bucharest")
			.setCountry("Romania");
	}

	@Test
	public void passForValidCustomer() {
		validator.validate(aValidCustomer());
	}

	@Test
//	public void whenCustomerHasNullName_throws() {
	public void throwsForCustomerWithNullName() {
		assertThrows(IllegalArgumentException.class,
			() -> validator.validate(aValidCustomer().setName(null)));
	}

	@Test
	public void throwsForCustomerWithAddressWithNullCity() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
			() -> validator.validate(aValidCustomer().setAddress(aValidAddress().setCity(null))));
		assertEquals("Missing address city", ex.getMessage());
	}
	@Test
	public void throwsForCustomerWithAddressNull() {
		assertThrows(IllegalArgumentException.class,
			() -> validator.validate(aValidCustomer().setAddress(null)));
	}

}