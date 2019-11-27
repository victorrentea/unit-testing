package ro.victor.unittest.builder;

import org.junit.Test;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {
		validator.validate(ObjectMother.aValidCustomer().build());
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		validator.validate(ObjectMother.aValidCustomer()
				.withName(null)
				.build());
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddress() {
		validator.validate(ObjectMother.aValidCustomer()
				.withAddress(null)
				.build());
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddressCity() {
		validator.validate(ObjectMother.aValidCustomer()
				.withAddressBuilder(ObjectMother.aValidAddress().withCity(null))
				.build());
	}


}

class ObjectMother {

	public static CustomerBuilder aValidCustomer() {
		return new CustomerBuilder()
				.withName("Nume")
				.withAddressBuilder(aValidAddress());
	}

	public static AddressBuilder aValidAddress() {
		return new AddressBuilder()
				.withCity("Bucale")
				.withStreetName("Preciziei")
				.withStreetNumber(12);
	}
}