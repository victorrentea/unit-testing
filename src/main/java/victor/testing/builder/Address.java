package victor.testing.builder;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Address {
	private String streetName;
	private Integer streetNumber;
	private String city;
	private String country;
}
