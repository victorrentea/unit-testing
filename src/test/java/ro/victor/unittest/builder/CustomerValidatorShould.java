package ro.victor.unittest.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.ExecutionException;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void yesSir() {
		validator.validate(ObjectMother.aValidCustomer());
	}
	@Test
	public void throwsForNullName() {
		expectedException.expectMessage("Missing customer name");
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