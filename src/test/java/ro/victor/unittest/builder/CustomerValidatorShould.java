package ro.victor.unittest.builder;

import org.junit.Test;
import ro.victor.unittest.tdd.tennis.TennisGame;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();


	@Test
	public void yesSir() {

		// builderul foloseste la creerea de date in diverse setupuri in mod fluent,
		// usor de citit de oameni
		validator.validate(aValidCustomer().build());
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNoName() {
		validator.validate(aValidCustomer().withName(null).build());
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNoAddressCity() {
		validator.validate(aValidCustomer()
				.withAddress(aValidAddress()
						.withCity(null)
						.build())
				.build());
	}

	private CustomerBuilder aValidCustomer() {
		return new CustomerBuilder()
				.withName("nume")
				.withAddress(aValidAddress()
						.build());
	}

	private AddressBuilder aValidAddress() {
		return new AddressBuilder()
				.withCity("City");
	}

}