package victor.testing.builder;



import org.junit.jupiter.api.Test;
import victor.testing.builder.MyException.ErrorCode;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


// Object Mother F.... (monoliti)
class DummyData {

//	"{name:'', labels:[], address:{}}"
	public static Customer aValidCustomer() {
		return new Customer()
			.setName("John")
			.addLabel("a","b")
			.addLabel("a","b")
			.addLabel("a","b")
			.addLabel("a","b")
			.setAddress(aValidAddress());
	}

	public static Address aValidAddress() {
		return new Address()
			.setCity("Bucharest")
			.setCountry("Romania");
	}
}

public class CustomerValidatorTest {

	private CustomerValidator validator = new CustomerValidator();

	@Test
	public void passForValidCustomer() {
		validator.validate(DummyData.aValidCustomer());
	}

	@Test
//	public void whenCustomerHasNullName_throws() {
	public void throwsForCustomerWithNullName() {
		assertThrows(IllegalArgumentException.class,
			() -> validator.validate(DummyData.aValidCustomer().setName(null)));
	}

	@Test
	public void throwsForCustomerWithAddressWithNullCity() {
		MyException ex = assertThrows(MyException.class,
			() -> validator.validate(DummyData.aValidCustomer().setAddress(DummyData.aValidAddress().setCity(null))));
		assertEquals(ErrorCode.NO_ADDRESS_CITY, ex.getCode());
	}
	@Test
	public void throwsForCustomerWithAddressNull() {
		assertThrows(IllegalArgumentException.class,
			() -> validator.validate(DummyData.aValidCustomer().setAddress(null)));
	}

}