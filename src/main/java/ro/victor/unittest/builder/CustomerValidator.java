package ro.victor.unittest.builder;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CustomerValidator {

	int validationCount  = 0;

	public int getValidationCount() {
		return validationCount;
	}

	public void validate(Customer customer) {
		validationCount++;
//		repo.save(stuff);
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
			throw new IllegalArgumentException("Missing address xcity");
		}
		if (isBlank(address.getStreetName())) {
			throw new IllegalArgumentException("Missing street xcity");
		}
	}
}
