package victor.testing.mutation;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
//@Builder // niciodata. nu are sens. caci poti sa-ti faci
//setteri generati de Lombok sa intoarca this.
public class Customer {
	private Long id;
	private String name;
	private String email;
	private List<String> labels = new ArrayList<>();
	private Address address = new Address();
	private Date createDate;
	private List<Coupon> coupons = new ArrayList<>();

	// Lombok genereaza asta: daca pui lombok.accessors.chain=true in lombok.config
//	public Customer setName(String name) {
//		this.name = name;
//		return this;
//	}
}
