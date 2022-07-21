package victor.testing.mutation;

import java.util.function.Predicate;

public class CustomerValidator {
	public void validate(Customer customer) {
		if (customer.getName() == null) {
			throw new IllegalArgumentException("Missing customer name");
		}
		if (customer.getEmail() == null) {
			throw new IllegalArgumentException("Missing customer email");
		}
		validateAddress(customer.getAddress());
		//etc
	}
	
	private void validateAddress(Address address) {
		if (address.getCity() == null) {
			throw new IllegalArgumentException("Missing address city");
		}
if (address.getCity() != null) {
	sendKafkaMessage("a");
}
		address.setCity(address.getCity().trim()); // mutate this
		if (address.getCity().length() < 3) { //.. or swap the ifs
			throw new IllegalArgumentException("Address city too short");
		}
	}

	private void sendKafkaMessage(String a) {
		throw new RuntimeException("Method not implemented");
	}
}
