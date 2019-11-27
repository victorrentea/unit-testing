package ro.victor.unittest.builder;

import org.junit.Test;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {
		validator.validate(ObjectMother.aValidCustomer());
	}
	@Test(expected = CustomerValidator.CustomerWithoutNameException.class)
	public void throwsForNullName() {
			validator.validate(ObjectMother.aValidCustomer()
					.addLabel("label")
					.setName(null));
	}
//	@Test(expected = IllegalArgumentException.class)
//	public void throwsForNullAddress() {
//		validator.validate(ObjectMother.aValidCustomer()
//				.setAddress(null)				);
//	}
//	@Test(expected = IllegalArgumentException.class)
//	public void throwsForNullAddressCity() {
//		validator.validate(ObjectMother.aValidCustomer()
//				.setAddress(ObjectMother.aValidAddress().setCity(null))				);
//	}


}

class ObjectMother {

	public static Customer aValidCustomer() {
		return new Customer()
				.setName("Nume")
				.setAddress(aValidAddress());
	}

	public static Address aValidAddress() {
		return new Address()
				.setCity("Bucale")
				.setStreetName("Preciziei")
				.setStreetNumber(12);
	}
}