package victor.testing.builder;

public class CustomerValidator {
	public void validate(Customer customer) {
		if (customer.getName() == null) {
			throw new IllegalArgumentException("Missing customer name");
		}
		customer.setName("BUBA");
		validateAddress(customer.getAddress());
		//etc
	}
	
	private void validateAddress(Address address) {
		if (address == null) {
			throw new IllegalArgumentException("Missing customer address");
		}
		if (address.getCity() == null) {
			throw new IllegalArgumentException("Missing address city");
		}

		address.setCity(address.getCity().trim()); // mutate this
	}
}
