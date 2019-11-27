package ro.victor.unittest.builder;

import cucumber.api.java.cs.A;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();
	
	@Test
	public void yesSir() {
		Customer customer = new Customer();
		customer.setName("John");
		customer.setAddress(new Address());
		customer.getAddress().setCity("Bucale");
		new CustomerValidator().validate(customer);
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullName() {
		Customer customer = new Customer();
		customer.setAddress(new Address());
		customer.getAddress().setCity("Bucale");
		new CustomerValidator().validate(customer);
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddress() {
		Customer customer = new Customer();
		customer.setName("John");
		new CustomerValidator().validate(customer);
	}
	@Test(expected = IllegalArgumentException.class)
	public void throwsForNullAddressCity() {
		Customer customer = new Customer();
		customer.setName("John");
		customer.setAddress(new Address());
		new CustomerValidator().validate(customer);
	}


}