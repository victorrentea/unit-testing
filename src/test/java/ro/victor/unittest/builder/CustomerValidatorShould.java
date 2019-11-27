package ro.victor.unittest.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static ro.victor.unittest.builder.ExceptiaMea.ErrorCode.*;

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
		expectedException.expect(new ExceptiaMeaCuCodul(CUSTOMER_WITHOUT_NAME));
		validator.validate(ObjectMother.aValidCustomer()
				.addLabel("label")
				.setName(null));
	}

	@Test
	public void throwsForNullAddress() {
		expectedException.expect(new ExceptiaMeaCuCodul(CUSTOMER_WITHOUT_ADDRESS));
		validator.validate(ObjectMother.aValidCustomer()
				.setAddress(null)				);
	}
	@Test
	public void throwsForNullAddressCity() {
		expectedException.expect(new ExceptiaMeaCuCodul(CUSTOMER_WITHOUT_ADDRESS_CITY));
		validator.validate(ObjectMother.aValidCustomer()
				.setAddress(ObjectMother.aValidAddress().setCity(null))				);
	}


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