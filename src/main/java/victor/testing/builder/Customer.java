package victor.testing.builder;

import lombok.*;
import lombok.ToString.Exclude;
import org.junit.experimental.theories.DataPoints;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
//@Data
//@Entity // merge
public class Customer {
	@Setter(AccessLevel.NONE)
//	@Id
	private Long id;
	private String name;
	private String phone;
	private List<String> labels = new ArrayList<>();
	private Address address;
	private Date createDate;
}
