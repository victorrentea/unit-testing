package victor.testing.mutation;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data // getter setter hash/eq tostring
// @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String email;
	private List<String> labels = new ArrayList<>();
	private Address address = new Address();
	private Date createDate;
	private List<Coupon> coupons = new ArrayList<>();

//	public Customer setName(String name) {
//		this.name = name;
//		return this;
//	}
}
