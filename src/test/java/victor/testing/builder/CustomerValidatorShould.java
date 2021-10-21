package victor.testing.builder;


import org.junit.jupiter.api.Test;
import victor.testing.builder.MyException.ErrorCode;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static victor.testing.builder.MyException.withCode;

public class CustomerValidatorShould {

	private CustomerValidator validator = new CustomerValidator();
	private Customer customer = TestData.aValidCustomer();

	@Test
	public void validCustomer() {
		validator.validate(customer);
	}

	@Test
	public void trimsCityName() {
		customer.getAddress().setCity("Iasi  ");

		validator.validate(customer);

		assertThat(customer.getAddress().getCity()).isEqualTo("Iasi");
	}

	@Test
	public void throwsForNullName() {
		customer.setName(null);

		assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
	}

	@Test
	public void throwsForNullEmail() {
		customer.setEmail(null);

		assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
	}

	@Test
	public void throwsForNullCity() {
		customer.getAddress().setCity(null);

		assertThrows(IllegalArgumentException.class, () -> validator.validate(customer));
	}
	@Test
	public void throwsForCityNameLessThan3() {
		customer.getAddress().setCity("YY");

		assertThatThrownBy(() -> validator.validate(customer))
			.isInstanceOf(IllegalArgumentException.class)
//			.hasMessageContaining("too short")
			.matches(withCode(ErrorCode.CUSTOMER_CITY_TOO_SHORT))
		;
	}



}


 // TODO
class Controller1 {

	 @Inject // Google guice
	 Service1 service1;// = new Service1();  // = mock()


	 public void method() {
		Service1.method(); // mai simplu de rupt dep asta decat

		 service1.methodDeInstanta(); // mai simplu de rupt dep asta decat

	}

 }

class Service1 {
	public static void method() {

	}
	public void methodDeInstanta() {

	}
}