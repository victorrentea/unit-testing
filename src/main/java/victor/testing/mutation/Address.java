package victor.testing.mutation;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Address {
	private String streetName;
	private Integer streetNumber;
	@NotNull
	@Size(min = 3)
	private String city;
	private Country country;
}
