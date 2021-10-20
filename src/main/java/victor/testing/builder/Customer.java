package victor.testing.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer {

	private String name;
	private String email;
	private List<String> labels = new ArrayList<>();
	private Address address = new Address();
	private Date createDate;
	private List<Coupon> coupons = new ArrayList<>();


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public Customer setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
		return this;
	}
}
