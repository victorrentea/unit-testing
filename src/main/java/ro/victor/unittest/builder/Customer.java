package ro.victor.unittest.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer {

	private String name;
	private String phone;
	private List<String> labels = new ArrayList<>();
	private Address address;
	private Date createDate;

	public Address getAddress() {
		return address;
	}

	public Customer setAddress(Address address) {
		this.address = address;
		return this;
	}


	//	{
//		new Customer()
//			.setAddress(new Address())
//			.setName("John")
//			.addLabel("label1");
//		new CustomerBuilder().persist();
//	}
//
//	class CustomerBuilder {
//		Customer customer;
//
//	public CustomerBuilder withAddress(Address address) {
//
//	}
//	public CustomerBuilder withAddress(AddressBuilder addressBuilder) {
//
//	}
//		public Customer build() {
//			return customer;
//		}
//		public void persist() {
//			// TestEntityManager.getInstance().save(customer);
//		}
//	}

	public void addLabel(String newLabel) {
		labels.add(newLabel);
	}

	public String getName() {
		return name;
	}

	public Customer setName(String name) {
		this.name = name;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
