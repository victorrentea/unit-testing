package victor.testing.builder;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomerValidatorTest {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void valid() {
		Customer customer = TestData.aValidCustomer();
		validator.validate(customer);
	}

	@Test
	public void throwsForNullName() {
		Customer customer = TestData.aValidCustomer().setName(null);

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> validator.validate(customer));
	}

	@Test
	public void throwsForNullAddress() {
		Customer customer = TestData.aValidCustomer().setAddress(null);

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> validator.validate(customer));
	}

}
