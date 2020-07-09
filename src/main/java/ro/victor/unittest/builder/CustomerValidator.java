package ro.victor.unittest.builder;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CustomerValidator {


	public void validate(Customer customer) {
		validateAddress(customer.getAddress());
		if (isBlank(customer.getName())) {
			throw new IllegalArgumentException("Missing customer name");
		}
		//etc
	}
	
	private void validateAddress(Address address) {
		if (address == null) {
			throw new IllegalArgumentException("Missing customer address");
		}
		if (isBlank(address.getCity())) {
			throw new IllegalArgumentException("Missing address xcity");
		}
	}
}
