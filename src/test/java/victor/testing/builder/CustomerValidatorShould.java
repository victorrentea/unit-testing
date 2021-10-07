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
		Customer customer = aCustomer();

		validator.validate(customer);
	}

	private Customer aCustomer() {
		// setteri fluenti generati de Lombok cu lombok.accessors.chain=true
		return new Customer()
			.setName("nume")
			.setAddress(new Address()
				.setCity("oras"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {

		// mod 1 trec tot MAI PUTIN campul cheie (name)
		Customer customer = new Customer();
		Address address = new Address();
		address.setCity("oras");
		customer.setAddress(address);
		// mod 2 stergi ce nu iti trebuie:
		// in general: pleci de la o instanta "standard" si o modifici
		customer = aCustomer().setName(null);

		validator.validate(customer);
	}


	@Test
	public void throwsForNullCity() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("address city");
		Customer customer = new Customer();
		customer.setName("ceva nenull");
		Address address = new Address();
		customer.setAddress(address);

		validator.validate(customer);
	}

	@Test
	public void throwsForNullCityNewAgeStyle() {
		Customer customer = new Customer();
		customer.setName("ceva nenull");
		Address address = new Address();
		customer.setAddress(address);

		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> validator.validate(customer));

//		assertEquals("Missing address city", ex.getMessage());
		String message = ex.getMessage();
//		Assert.assertTrue(message.contains("city"));

		assertThat(ex.getMessage()).contains("city");
	}



}