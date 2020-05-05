package ro.victor.unittest.builder;

import org.apache.commons.lang3.StringUtils;

public class CustomerValidator {

	public void validate(Customer customer) {
		if (StringUtils.isBlank(customer.getName())) {
			throw new MyException(MyException.ErrorCode.MISSING_CUSTOMER_NAME);
		}
		validateAddress(customer.getAddress());
		//etc
	}

	private void validateAddress(Address address) {
		if (address == null) {
			throw new MyException(MyException.ErrorCode.MISSING_CUSTOMER_ADDRESS);
		}
		double doi = Math.sqrt(4);
		System.out.println(doi);
		if (StringUtils.isBlank(address.getCity())) {
			throw new MyException(MyException.ErrorCode.MISSING_CUSTOMER_ADDRESS_CITY);
		}
	}
}

class MyException extends RuntimeException {
	public enum ErrorCode {
		GENERAL, MISSING_CUSTOMER_NAME, MISSING_CUSTOMER_ADDRESS, MISSING_CUSTOMER_ADDRESS_CITY
	}
	private final ErrorCode errorCode;

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public MyException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public MyException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public MyException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public MyException(Throwable cause, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.errorCode = errorCode;
	}
}
