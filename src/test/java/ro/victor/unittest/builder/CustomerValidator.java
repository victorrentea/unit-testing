package ro.victor.unittest.builder;

import org.apache.commons.lang3.StringUtils;

import static ro.victor.unittest.builder.ExceptiaMea.ErrorCode.*;


class ExceptiaMea extends RuntimeException {
	enum ErrorCode {
		CUSTOMER_WITHOUT_NAME,
		CUSTOMER_WITHOUT_ADDRESS,
		CUSTOMER_WITHOUT_ADDRESS_CITY
	}
	private final ErrorCode errorCode;
	ExceptiaMea(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}

public class CustomerValidator {


	public void validate(Customer customer) {
		if (StringUtils.isBlank(customer.getName())) {
			throw new ExceptiaMea(CUSTOMER_WITHOUT_NAME);
		}
		validateAddress(customer.getAddress());
		//etc
	}

	private void validateAddress(Address address) {
		if (address == null) {
			throw new ExceptiaMea(CUSTOMER_WITHOUT_ADDRESS);
		}
		if (StringUtils.isBlank(address.getCity())) {
			throw new ExceptiaMea(CUSTOMER_WITHOUT_ADDRESS_CITY);
		}
	}
}
