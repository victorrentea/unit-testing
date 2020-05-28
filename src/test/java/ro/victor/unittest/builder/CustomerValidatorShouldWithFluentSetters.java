package ro.victor.unittest.builder;

import org.junit.Test;

import java.util.Date;

public class CustomerValidatorShouldWithFluentSetters {

	private CustomerValidator validator = new CustomerValidator();


	@Test
	public void yesSir() {

		// builderul foloseste la creerea de date in diverse setupuri in mod fluent,
		// usor de citit de oameni
		validator.validate(aValidCustomer());
	}

	// NU O FACE: public static --> nivelul 2 va fi sa o muti intr-o clasa centrala
	// care va avea doar metode statice care produc date 'realisete' de biz valide
	// apoi si alti vor iubi acea clasa. --> Object Mother ...
	// si apoi, tu peste 6 luni vei comenta o linie din una din acele factor methods statice
	// altii iti vor multumi
	//
	private Customer aValidCustomer() {
		return new Customer()
				.setName("nume")
				.setPhone("8989989")
				.setCreateDate(new Date())
				.setAddress(aValidAddress());
	}

	private Address aValidAddress() {
		return new Address()
				.setCity("City");
	}

}