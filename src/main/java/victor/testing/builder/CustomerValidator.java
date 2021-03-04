package victor.testing.builder;

import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.StringUtils.isBlank;

class NotificationSender {

	public void sendEmailCaEOK() {

	}
}
public class CustomerValidator {
	NotificationSender sender = new NotificationSender();
	public void validate(Customer customer) {
		validateAddress(customer.getAddress());
		if (isBlank(customer.getName())) {
			throw new IllegalArgumentException("Missing customer name");
		}
		sender.sendEmailCaEOK(); // linia asta nu e verificata acum de teste.
		//etc
	}
	
	private void validateAddress(Address address) {
		if (address == null) {
			throw new IllegalArgumentException("Missing customer address");
		}
		if (isBlank(address.getCity())) {
			throw new IllegalArgumentException("Missing address xcity");
		}
	}
}
