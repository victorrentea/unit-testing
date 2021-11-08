package victor.testing.builder;

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
		if (address == null) {
			throw new IllegalArgumentException("error.customer.address.missing");
		}
		if (address.getCity() == null) {
			throw new IllegalArgumentException("Missing address city");
		}
		if (address.getCity().length() < 3) {
			throw new IllegalArgumentException("Missing address city");
		}

		address.setCity(address.getCity().trim()); // mutate this
	}
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
