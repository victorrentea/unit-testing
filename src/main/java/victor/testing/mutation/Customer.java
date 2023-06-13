package victor.testing.mutation;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Customer {
	private Long id;
	private String name;
	private String email;
	private List<String> labels = new ArrayList<>();
	private Address address = new Address();
	private Date createDate;
	private List<Coupon> coupons = new ArrayList<>();


	// chainable setters see lombok.config
//	public Customer setName(String name) {
//		this.name = name;
//		return this;
//	}
}
