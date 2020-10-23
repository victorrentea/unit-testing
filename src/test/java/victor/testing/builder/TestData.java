package victor.testing.builder;

// Object Mother 
public class TestData {

	public static Customer aValidCustomer() { // depind 105 de teste
		return new Customer()
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setName("John")
				.setAddress(TestData.aValidAddress());
	}

	public static Address aValidAddress() {
		return new Address()
				.setCity("Bucuresti, Orasul Smogului");
	}
	
}