package ro.victor.unittest.builder;

public class CustomerBuilder {
	private final Customer customer = new Customer();

	public CustomerBuilder withName(String name) {
		customer.setName(name);
		return this;
	}


	public Customer build() {
		return customer;
	}

	public CustomerBuilder withAddress(Address address) {
		customer.setAddress(address);
		return this;
	}

	public CustomerBuilder withAddressBuilder(AddressBuilder addressBuilder) {
		customer.setAddress(addressBuilder.build());
		return this;
	}
}