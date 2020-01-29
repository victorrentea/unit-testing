package ro.victor.unittest.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.rules.ExpectedException.none;

public class CustomerValidatorShould {


	private CustomerValidator validator = new CustomerValidator();

	private Customer aValidCustomer() {
		return new Customer()
				.setName("John")
				.setAddress(aValidAddress());
	}

	private Address aValidAddress() {
		return new Address()
				.setCity("Bucuresti")
				.setStreetName("Pompei");
	}

	@Test
	public void beOK() {
		validator.validate(aValidCustomer());
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwForCustomerWithNoName() {
		Customer customer = aValidCustomer().setName(null);
		validator.validate(customer);
	}

	@Test // ASA NU!
	public void throwForCustomerWithNoName_exMessage() {
		try {
			Customer customer = aValidCustomer().setName(null);
			validator.validate(customer);
			fail(); // sic!
		} catch (Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertThat(e.getMessage()).contains("name");
		}
	}

	@Rule
	public ExpectedException expectedException = none();

	@Test // ASA DA!
	public void throwForCustomerWithNoAddressCity_cuMatcher() {
		expectedException.expect(new ExceptiaMeaMatcher(
				ExceptiaMea.ErrorCode.CUSTOMER_WITH_NO_ADDRESS_CITY));

		Customer customer = aValidCustomer()
				.setAddress(aValidAddress().setCity(null));
		validator.validate(customer);
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwForCustomerWithNoAddressCity() {
		Customer customer = aValidCustomer()
				.setAddress(aValidAddress().setCity(null));
		validator.validate(customer);
	}


}