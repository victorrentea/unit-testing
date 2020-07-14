package ro.victor.unittest.builder;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CustomerValidator {

	public void validate(Customer customer) {
		if (isBlank(customer.getName())) {
			throw new MyAppException(ErrorCode.CUSTOMER_WITHOUT_NAME);
		}
		validateAddress(customer.getAddress());
		//etc
	}

	private void validateAddress(Address address) {
		if (address == null) {
			throw new MyAppException(ErrorCode.CUSTOMER_WITHOUT_ADDRESS);
		}
		if (isBlank(address.getCity())) {
			throw new IllegalArgumentException("Missing address city");
		}
	}
}

