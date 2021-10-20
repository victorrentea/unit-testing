package victor.testing.builder;

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
		if (address.getCountry() == null) {
			throw new IllegalArgumentException("Missing address country");
		}
		address.setCity(address.getCity().trim()); // mutate this
	}
}
