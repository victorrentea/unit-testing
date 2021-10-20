package victor.testing.builder;


import org.junit.jupiter.api.Test;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {
		Customer customer = new Customer();
		customer
			.setName("with")
			.setAddress(new Address()
				.setCity("Iasi"));

		validator.validate(customer);
	}

	// TODO maine exceptiile
}