package ro.victor.unittest.builder;

public class CustomerBuilder {
	private final Customer customerUnderConstruction = new Customer();

	public CustomerBuilder withName(String name) {
		customerUnderConstruction.setName(name);
		return this;
	}


	public Customer build() {
		return customerUnderConstruction;
	}

  public CustomerBuilder withAddress(Address address) {
		customerUnderConstruction.setAddress(address);
		return this;
  }
}