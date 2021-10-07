package victor.testing.builder;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void yesSir() {
		Customer customer = TestData.aCustomer();

		validator.validate(customer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		Customer customer = TestData.aCustomer().setName(null);

		validator.validate(customer);
	}


	@Test
	public void throwsForNullCity() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("address city");
		Customer customer = TestData.aCustomer();
		customer.getAddress().setCity(null);

		validator.validate(customer);
	}

	@Test
	public void throwsForNullCityNewAgeStyle() {
		Customer customer = TestData.aCustomer();
		customer.getAddress().setCity(null);

		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> validator.validate(customer));

		assertThat(ex.getMessage()).contains("city");
	}

	@Test
	public void throwsForNullAddress() {
		Customer customer = TestData.aCustomer().setAddress(null);

		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> validator.validate(customer));

		assertThat(ex.getMessage()).contains("address");
	}



}