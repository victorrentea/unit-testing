package victor.testing.builder;

public class CustomerBuilder {
	private final Customer customer = new Customer();

	public CustomerBuilder withName(String name) {
		customer.setName(name);
		return this;
	}


	public Customer build() {
		return customer;
	}
}