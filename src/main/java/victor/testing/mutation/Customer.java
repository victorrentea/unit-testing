package victor.testing.mutation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer {
	private Long id;
	private String name;
	private String email;
	private List<String> labels = new ArrayList<>();
	private Address address = new Address();
	private Date createDate;
	private List<Coupon> coupons = new ArrayList<>();

	public Customer() {
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return this.email;
	}

	public List<String> getLabels() {
		return this.labels;
	}

	public Address getAddress() {
		return this.address;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public List<Coupon> getCoupons() {
		return this.coupons;
	}

	public Customer setId(Long id) {
		this.id = id;
		return this;
	}

	public Customer setName(String name) {
		this.name = name;
		return this;
	}

	public Customer setEmail(String email) {
		this.email = email;
		return this;
	}

	public Customer setLabels(List<String> labels) {
		this.labels = labels;
		return this;
	}

	public Customer setAddress(Address address) {
		this.address = address;
		return this;
	}

	public Customer setCreateDate(Date createDate) {
		this.createDate = createDate;
		return this;
	}

	public Customer setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
		return this;
	}

	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof Customer)) return false;
		final Customer other = (Customer) o;
		if (!other.canEqual((Object) this)) return false;
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
		final Object this$email = this.getEmail();
		final Object other$email = other.getEmail();
		if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
		final Object this$labels = this.getLabels();
		final Object other$labels = other.getLabels();
		if (this$labels == null ? other$labels != null : !this$labels.equals(other$labels)) return false;
		final Object this$address = this.getAddress();
		final Object other$address = other.getAddress();
		if (this$address == null ? other$address != null : !this$address.equals(other$address)) return false;
		final Object this$createDate = this.getCreateDate();
		final Object other$createDate = other.getCreateDate();
		if (this$createDate == null ? other$createDate != null : !this$createDate.equals(other$createDate))
			return false;
		final Object this$coupons = this.getCoupons();
		final Object other$coupons = other.getCoupons();
		if (this$coupons == null ? other$coupons != null : !this$coupons.equals(other$coupons)) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof Customer;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		final Object $name = this.getName();
		result = result * PRIME + ($name == null ? 43 : $name.hashCode());
		final Object $email = this.getEmail();
		result = result * PRIME + ($email == null ? 43 : $email.hashCode());
		final Object $labels = this.getLabels();
		result = result * PRIME + ($labels == null ? 43 : $labels.hashCode());
		final Object $address = this.getAddress();
		result = result * PRIME + ($address == null ? 43 : $address.hashCode());
		final Object $createDate = this.getCreateDate();
		result = result * PRIME + ($createDate == null ? 43 : $createDate.hashCode());
		final Object $coupons = this.getCoupons();
		result = result * PRIME + ($coupons == null ? 43 : $coupons.hashCode());
		return result;
	}

	public String toString() {
		return "Customer(id=" + this.getId() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", labels=" + this.getLabels() + ", address=" + this.getAddress() + ", createDate=" + this.getCreateDate() + ", coupons=" + this.getCoupons() + ")";
	}
}
