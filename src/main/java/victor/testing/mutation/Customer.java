package victor.testing.mutation;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// imagine @Entity
@Data
//@Builder // GRESIT PR reject.
//   in schimb daca vrei sa creezi obiectul mai 'compact' poti sa faci setteri sa intoarca 'this'
public class Customer {
	private Long id;
	private String name;
	private String email;
	private List<String> labels = new ArrayList<>();
	private Address address = new Address();
	private Date createDate;
	private List<Coupon> coupons = new ArrayList<>();

	public String getName() {
		return name;
	}

	public Customer setName(String name) {
		this.name = name;
		return this;
	}
}
