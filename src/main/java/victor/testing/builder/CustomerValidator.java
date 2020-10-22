package victor.testing.builder;

import static org.apache.commons.lang3.StringUtils.isBlank;

import victor.testing.builder.MyException.ErrorCode;

public class CustomerValidator {
 
	public void validate(Customer customer) {
//		customer.setLabels(null);
		if (isBlank(customer.getName())) {
			throw new IllegalArgumentException("Missing customer name");
		}
		validateAddress(customer.getAddress());
		//etc
	}
	
	private void validateAddress(Address address) {
		if (address == null) {
			throw new MyException(ErrorCode.NO_ADDRESS);
		}
		if (isBlank(address.getCity())) {
			throw new MyException(ErrorCode.NO_CITY);
		}
	}
}


class MyException extends RuntimeException {
	enum ErrorCode {
		NO_NAME,
		NO_ADDRESS,
		NO_CITY
		
	}
	private final ErrorCode code;
	public MyException(ErrorCode code) {
		this.code = code;
	}
	
	public ErrorCode getCode() {
		return code;
	}
	
	
	
	
	
}