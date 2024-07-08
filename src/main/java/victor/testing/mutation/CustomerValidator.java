package victor.testing.mutation;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public class CustomerValidator {
	// example of client code
//	@PostMapping("/customer")
//	public void createCustomer(@RequestBody Customer customer) {
//		new CustomerValidator().validate(customer);
//	}

	public static void main(String[] args) {
		CustomerValidator validator = new CustomerValidator();
		validator.validatedWithAnnotations(new Customer());

	}
//	@Validated // AOP
	public void validatedWithAnnotations(Customer customer) {
		// normally injected by the framework or created:
		Validator validator = javax.validation.Validation.buildDefaultValidatorFactory().getValidator();

		Set<ConstraintViolation<Customer>> errors = validator.validate(customer);
		// or, better via some AOP (AspectJ, Spring AOP, etc) using anotations on
		System.out.println("Validation errors: ");
		errors.forEach(System.out::println);
	}


	public void validate(Customer customer) {
		if (customer == null) {
			throw new IllegalArgumentException("Missing customer");
		}
		if (customer.getName() == null) {
			throw new IllegalArgumentException("Missing customer name");
		}
		if (customer.getEmail() == null) {
			throw new IllegalArgumentException("Missing email");
		}
		validateAddress(customer.getAddress());
	}
	
	private void validateAddress(Address address) {
		if (address.getCity() == null) {
			throw new IllegalArgumentException("Missing address city");
		}
		address.setCity(address.getCity().trim()); // mutate this
		if (address.getCity().length() < 3) { //.. or swap the ifs
			throw new IllegalArgumentException("Address city too short");
		}
	}
}


