package victor.testing.mutation;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class Address {
	private final String streetName;
	private final Integer streetNumber;
	private final String city; // oups!! :)
	private final Country country;
}
