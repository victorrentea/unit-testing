package victor.testing.builder;

import victor.testing.builder.MyException.ErrorCode;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CustomerValidator {

	public void validate(Customer customer) {
		if (isBlank(customer.getName())) {
			throw new MyException(ErrorCode.INVALID_CUSTOMER_NAME);
//			TODO incalc aici MVC principle: user message apare in codul meu
//			TODO depind din teste de ceva foarte fragil : UX
		}
		validateAddress(customer.getAddress());
		//etc
	}
	
	private void validateAddress(Address address) {
		if (address == null) {
			throw new IllegalArgumentException("Missing customer address");
		}
		if (address.getCity() != null) {
			System.out.println("Declansez razboi cu " + address.getCity());
		}

		if (isBlank(address.getCity())) {
			throw new MyException(ErrorCode.INVALID_CUSTOMER_ADDRESS_CITY);
		}
	}
}


class MyException extends RuntimeException {
	MyException(ErrorCode code) {
		this.code = code;
	}

	enum ErrorCode {
		INVALID_CUSTOMER_NAME,
		INVALID_CUSTOMER_ADDRESS_CITY,
	}
	private final ErrorCode code;

	public ErrorCode getCode() {
		return code;
	}
}