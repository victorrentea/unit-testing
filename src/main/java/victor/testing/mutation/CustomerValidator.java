package victor.testing.mutation;

import org.testcontainers.shaded.com.trilead.ssh2.sftp.ErrorCodes;

import java.util.function.Predicate;

public class CustomerValidator {
	public void validate(Customer customer) {
		if (customer.getName() == null) {
//			throw new MyException(ErrorCodesEnum.MISSING_NAME);
				// better? decouple the string from the code
				// cost: +1 enum with values for every error exit point here
				// Definetely better design if the code would be translated to the client to a certain strign messages.properties
			throw new IllegalArgumentException("Missing the customer name");
			// who is the user of the above string: developer
		}
		if (customer.getEmail() == null) {
			throw new IllegalArgumentException("Missing customer email");
		}
		validateAddress(customer.getAddress());
	}
	
	private void validateAddress(Address address) {
		if (address.getCity() == null) {
			throw new IllegalArgumentException("Missing address city name");
		}
		address.setCity(address.getCity().trim()); // mutate this
		if (address.getCity().length() < 3) { //.. or swap the ifs
			throw new IllegalArgumentException("Address city too short");
		}
	}
}


