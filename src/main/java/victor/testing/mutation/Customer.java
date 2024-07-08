package victor.testing.mutation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Data // = @Getter @Setter @ToString @EqualsAndHashCode @RequiredArgsConstructor
@Getter
@Setter
public class Customer {
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private String email;
	private List<String> labels = new ArrayList<>();
	@Valid
	@NotNull
	private Address address = new Address();
	private Date createDate;
	private List<Coupon> coupons = new ArrayList<>();

	public Customer setName(String name) { // if you delete this, lombok will generate it back
		this.name = name;
		return this; // 'fluent'/'chainable' setters
	}
}
