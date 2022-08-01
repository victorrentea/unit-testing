package victor.testing.mutation;


class CustomerValidationException extends RuntimeException {
	private final String fieldInError;

	CustomerValidationException(String fieldInError) {
		this.fieldInError = fieldInError;
	}

	public String getFieldInError() {
		return fieldInError;
	}
}
public class CustomerValidator {
	public void validate(Customer customer) {
		if (customer.getName() == null) {
			throw new CustomerValidationException("name");
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
		address.setCity(address.getCity().trim()); // mutate this
		if (address.getCity().length() < 3) { //.. or swap the ifs
			throw new IllegalArgumentException("Address city too short");
		}
	}
}


