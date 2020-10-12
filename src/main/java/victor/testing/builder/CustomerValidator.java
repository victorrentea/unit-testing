package victor.testing.builder;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CustomerValidator {

	public void validate(Customer customer) {
		if (isBlank(customer.getName())) {
			throw new IllegalArgumentException("Missing customer name");
		}
		validateAddress(customer.getAddress());
		//etc
	}
	
	private void validateAddress(Address address) {

		if (address == null) {
			throw new IllegalArgumentException("Missing customer address");
		}
		if (isBlank(address.getCity())) {
			throw new IllegalArgumentException("The address of a customer must be set");
		}
	}
}
