package victor.testing.mutation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Data // = @Getter @Setter @ToString @EqualsAndHashCode @RequiredArgsConstructor
@Getter
@Setter
public class Customer {
	private Long id;
	private String name;
	private String email;
	private List<String> labels = new ArrayList<>();
	private Address address = new Address();
	private Date createDate;
	private List<Coupon> coupons = new ArrayList<>();

	public Customer setName(String name) { // if you delete this, lombok will generate it back
		this.name = name;
		return this; // 'fluent'/'chainable' setters
	}
}
