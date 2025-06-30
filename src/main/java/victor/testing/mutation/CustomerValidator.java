package victor.testing.mutation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerValidator {
	public void validate(Customer customer) {
		if (customer.getName() == null) {
			throw new IllegalArgumentException("Missing customer name");
		}
		if (customer.getEmail() == null) {
			throw new IllegalArgumentException("Missing customer email");
		}
		validateAddress(customer.getAddress());
	}
	
	private void validateAddress(Address address) {
		if (address.getCity() == null) {
			throw new IllegalArgumentException("Missing address city");
		}
		log.info("Some info for developers used for debugging/tracing normal flow");
		log.warn("[ALARM-1] NOT SUPPORTED VALUE"); // or error
		address.setCity(address.getCity().trim()); // mutate this
		if (address.getCity().length() < 3) { //.. or swap the ifs
			throw new IllegalArgumentException("Address city too short");
		}
	}
}


