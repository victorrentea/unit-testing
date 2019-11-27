package ro.victor.unittest.builder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

import static ro.victor.unittest.builder.ExceptiaMea.ErrorCode.*;

public class CustomerValidatorShould extends CommonTestBase {

	private CustomerValidator validator = new CustomerValidator();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	public CustomerValidatorShould() {
		System.out.println("#sieu");
	}

	@Before
	public void deSus2() {
		System.out.println("in clasa mea de test");
	}
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