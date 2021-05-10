package victor.testing.builder;

import org.junit.Test;

public class CustomerValidatorTest {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {


		Customer customer = new Customer()
			.setName("John")
			.addLabels("l1","l2")
			.setAddress(new Address()
				.setCity("Paris")
			)
			;


	}

}