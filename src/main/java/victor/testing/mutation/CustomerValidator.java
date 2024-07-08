package victor.testing.mutation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class CustomerValidator {
	// example of client code
//	@PostMapping("/customer")
//	public void createCustomer(@RequestBody Customer customer) {
//		new CustomerValidator().validate(customer);
//	}

	public void validate(Customer customer) {
		if (customer == null) {
			throw new IllegalArgumentException("Missing customer");
		}
		if (customer.getName() == null) {
			throw new IllegalArgumentException("Missing customer name");
		}
		if (customer.getEmail() == null) {
			throw new IllegalArgumentException("Missing email");
		}
		validateAddress(customer.getAddress());
	}
	
	private void validateAddress(Address address) {
		if (address.getCity() == null) {
			throw new IllegalArgumentException("Missing address city");
		}
		address.setCity(address.getCity().trim()); // mutate this
		if (address.getCity().length() < 3) { //.. or swap the ifs
			throw new IllegalArgumentException("Address city too short");
		}
	}
}


