package victor.testing.mutation;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

@Slf4j
public class CustomerValidator {
	public void validate(Customer customer) {
		if (customer.getName() == null) {
//			log.error("Missing customer name"); // mai bine pune mesajul in exceptie
			throw new IllegalArgumentException();
		}
		customer.setName(customer.getName().trim()); // mutate this
		if (customer.getEmail() == null) {
			throw new IllegalArgumentException("Missing customer email");
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


