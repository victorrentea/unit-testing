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
		//etc
	}
	
	private void validateAddress(Address address) {
		if (address.getCity() == null) {
			throw new IllegalArgumentException("Missing address city");
		}
		address.setCity(address.getCity().trim()); // mutate this
		if (address.getCity().length() < 3) {
			throw new IllegalArgumentException("Address city too short");
		}
	}
}


class MyException extends RuntimeException {
	MyException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	// + other 12 overloads, taking a message string, a cause exception, and combinations

	public enum ErrorCode {
		CUSTOMER_MISSING_CITY,
		CUSTOMER_TOO_SHORT_CITY,
		GENERAL
	}
	private final ErrorCode errorCode;

	// to use in tests:
	public static <T extends Throwable> Predicate<T> hasCode(ErrorCode errorCode) {
		return e -> (e instanceof MyException && ((MyException) e).errorCode == errorCode);
	}
}