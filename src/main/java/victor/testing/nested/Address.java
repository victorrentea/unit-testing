package victor.testing.nested;

import lombok.Data;

@Data
public class Address {
	private String streetName;
	private Integer streetNumber;
	private String city;
	private Country country;

}
