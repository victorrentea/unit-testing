package victor.testing.builder;

import org.junit.Test;

public class CustomerValidatorTest {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {
		Customer customer = TestData.aValidCustomer();
		validator.validate(customer);

	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		Customer customer = TestData.aValidCustomer().setName(null);
		validator.validate(customer);

	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddressCity() {
		Customer customer = TestData.aValidCustomer().setAddress(
			TestData.aValidAddress().setCity(null)
		);
		validator.validate(customer);

	}

}

// Object Mother
class TestData {

	public static Customer aValidCustomer() {
		return new Customer()
			.setName("John")
			.setAddress(aValidAddress()
			);
	}

	public static Address aValidAddress() {
		return new Address()
			.setCity("Paris");
	}
}