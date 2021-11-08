package victor.testing.builder;

import victor.testing.builder.MyException.ErrorCode;

import java.util.function.Predicate;

public class CustomerValidator {
	public void validate(Customer customer) {
		if (customer.getName() == null) {
			throw new IllegalArgumentException("Missing customer name");
		}
		if (customer.getEmail() == null) {
			 throw new IllegalArgumentException("Missing customer email");
		}
		validateAddress(customer.getAddress());
	}
	
	private void validateAddress(Address address) {
		if (address.getCity() == null) {
//			throw new IllegalArgumentException("Missing address city");
			throw new NoAddressCity();
		}
		address.setCity(address.getCity().trim()); // mutate this
		if (address.getCity().length() < 5) {
//			throw new IllegalArgumentException("City name too short");
			throw new MyException(ErrorCode.CUSTOMER_CITY_TOO_SHORT);
		}
	}
}

class NoAddressCity extends RuntimeException{ // cu miile -- nu e o idee buna

}

class MyException extends RuntimeException {
	MyException(ErrorCode code) {
		this.code = code;
	}

	enum ErrorCode {
		CUSTOMER_CITY_TOO_SHORT("error.customer.city.too-short"),
		GENERAL("error.general");

		private final String messageKey;
		ErrorCode(String messageKey) {
			this.messageKey = messageKey;
		}
		public String getMessageKey() {
			return messageKey;
		}
	}
	private final ErrorCode code;

	public ErrorCode getCode() {
		return code;
	}

	public static Predicate<Throwable> withCode(ErrorCode expectedErrorCode) {
		return e -> ((MyException)e).getCode() == expectedErrorCode;
	}
}
