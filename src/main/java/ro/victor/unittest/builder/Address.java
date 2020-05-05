package ro.victor.unittest.builder;

public class Address {
	private String streetName;
	private Integer streetNumber;
	private String city;
	private String country;
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	Address setCustomer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public Address setCity(String city) {
		this.city = city;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public final Integer getStreetNumber() {
		return streetNumber;
	}

	public final void setStreetNumber(Integer streetNumber) {
		this.streetNumber = streetNumber;
	}
	
}
