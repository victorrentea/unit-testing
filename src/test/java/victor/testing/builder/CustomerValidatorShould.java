package victor.testing.builder;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomerValidatorShould {
	CustomerValidator validator;



	@Test// (expected=IllegalArgumentException.class) junit 4
	void test() {
		// given
		Customer customer = new Customer();
		CustomerValidator validator = new CustomerValidator();

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> validator.validate(customer));

		// tehnic, tot aia:
//		try {
//			validator.validate(customer)
//			Assert.fail();
//		}catch (IllegalArgumentException e) {
//			return;
//		}
//		Assert.fail();
	}

}