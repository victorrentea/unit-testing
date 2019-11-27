package ro.victor.unittest.builder;

import cucumber.api.java.cs.A;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();
	
	@Test
	public void beOK() {
		Customer customer = new Customer();
		customer.setName("John");
		customer.setAddress(new Address());
		customer.getAddress().setCity("Bucale");
		new CustomerValidator().validate(customer);
	}


}