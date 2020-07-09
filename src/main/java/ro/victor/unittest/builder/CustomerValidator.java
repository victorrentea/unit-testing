package ro.victor.unittest.builder;

import org.apache.commons.lang3.StringUtils;
import ro.victor.unittest.MyException;
import ro.victor.unittest.MyException.ErrorCode;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CustomerValidator {


	public void validate(Customer customer) {
		validateAddress(customer.getAddress());
		if (isBlank(customer.getName())) {
			throw new MyException(ErrorCode.MISSING_CUSTOMER_NAME);
		}
		//etc
	}
	
	private void validateAddress(Address address) {
		if (address == null) {
			throw new MyException(ErrorCode.MISSING_CUSTOMER_ADDRESS);
		}
		if (isBlank(address.getCity())) {
			throw new IllegalArgumentException("Missing address xcity");
		}
	}
}

