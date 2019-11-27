package ro.victor.unittest.builder;

import org.junit.Before;
import org.junit.Test;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	private Customer customer = new Customer();
	public CustomerValidatorShould() {
		System.out.println("Aoleu. O instanta de clasa de test noua");
	}

	@Before
	public void setup() {
		customer = new CustomerBuilder()
				.withName("Nume")
				.withAddress(new AddressBuilder()
						.withCity("Bucale")
						.withStreetNumber(12)
						.build())
				.build();
	}

	@Test
	public void yesSir() {
		validator.validate(customer);
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		customer.setName(null);
		validator.validate(customer);
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddress() {
		customer.setAddress(null);
		validator.validate(customer);
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddressCity() {
		customer.getAddress().setCity(null);
		validator.validate(customer);
	}


}