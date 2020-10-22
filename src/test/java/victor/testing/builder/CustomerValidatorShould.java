package victor.testing.builder;

import org.junit.Test; 

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void yesSir() {
		Customer customer = new Customer();
		customer.setName("John");
		Address address = new Address();
		address.setCity("Bucuresti, Orasul Smogului");
		customer.setAddress(address);
		validator.validate(customer);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		Customer customer = new Customer();
		Address address = new Address();
		address.setCity("Bucuresti, Orasul Smogului");
		customer.setAddress(address);
		validator.validate(customer);
	}

}