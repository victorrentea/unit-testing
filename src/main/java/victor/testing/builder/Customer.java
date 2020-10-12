package victor.testing.builder;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

 //@Data - niciodata pe entitat!!
//@Entity
public class Customer {

	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private String phone;
	private List<String> labels = new ArrayList<>();
	@Getter
	@Setter
	private Address address;
	@Getter
	@Setter
	private Date createDate;

	//3-4 mici metode test-friend
	 public Customer addLabel(String... label) {
		 labels.addAll(Arrays.asList(label));
		 return this;
	 }
}
