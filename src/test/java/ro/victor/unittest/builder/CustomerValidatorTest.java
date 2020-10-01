package ro.victor.unittest.builder;

import org.junit.Test;

public class CustomerValidatorTest {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {
		Customer customer = TestData.aValidCustomer();

		validator.validate(customer);
	}

	@Test(expected = IllegalArgumentException.class)
//	public void test2() {
//	public void whenCustomerHasNoName_thenValidationFails() {
	public void throwsForCustomerWithNoName() {
		validator.validate(TestData.aValidCustomer().setName(null));
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithNoPhone() {
		validator.validate(TestData.aValidCustomer().setPhone(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForCustomerWithAddressWithNoCity() {
		validator.validate(TestData.aValidCustomer().setAddress(
			TestData.aValidAddress()
				.setCity(null)));
	}

}