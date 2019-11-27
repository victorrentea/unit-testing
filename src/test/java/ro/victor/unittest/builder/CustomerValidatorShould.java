package ro.victor.unittest.builder;

import org.junit.Before;
import org.junit.Test;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	public CustomerValidatorShould() {
		System.out.println("Aoleu. O instanta de clasa de test noua");
	}

	private CustomerBuilder aValidCustomer() {
		return new CustomerBuilder()
				.withName("Nume")
				.withAddressBuilder(aValidAddress());
	}

	private AddressBuilder aValidAddress() {
		return new AddressBuilder()
				.withCity("Bucale")
				.withStreetName("Preciziei")
				.withStreetNumber(12);
	}

	@Test
	public void yesSir() {
		validator.validate(aValidCustomer().build());
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		validator.validate(aValidCustomer()
				.withName(null)
				.build());
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddress() {
		validator.validate(aValidCustomer()
				.withAddress(null)
				.build());
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddressCity() {
		validator.validate(aValidCustomer()
				.withAddressBuilder(aValidAddress().withCity(null))
				.build());
	}


}