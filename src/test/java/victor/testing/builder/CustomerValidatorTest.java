package victor.testing.builder;



import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerValidatorTest {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void passForValidCustomer() {


		Customer customer = new CustomerBuilder()
			.withName("John")
			.withAddress(new AddressBuilder()
				.withCity("Bucuresti")
				.build())
			.build();
		validator.validate(customer);
	}

	@Test
//	public void whenCustomerHasNullName_throws() {
	public void throwsForCustomerWithNullName() {
		Customer customer = new Customer();
		Address address = new Address();
		address.setCity("Bucuresti");
		customer.setAddress(address);

		assertThrows(IllegalArgumentException.class,
			() -> validator.validate(customer));
	}

	@Test
	public void throwsForCustomerWithAddressWithNullCity() {
		Customer customer = new Customer();
		customer.setName("John");
		Address address = new Address();
		customer.setAddress(address);

		assertThrows(IllegalArgumentException.class,
			() -> validator.validate(customer));
	}

}