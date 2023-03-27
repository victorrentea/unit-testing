package victor.testing.mutation;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Value @Builder // lombok's immutable class
@Data
public class Customer {
	private Long id;
	private String name;
	private String email;
	private List<String> labels = new ArrayList<>();
	private Address address = new Address();
	private Date createDate;
	private List<Coupon> coupons = new ArrayList<>();

	public Customer setName(String name) {
		this.name = name;
		return this;
	}
}
