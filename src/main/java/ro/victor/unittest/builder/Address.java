package ro.victor.unittest.builder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Address {
	private String streetName;
	private Integer streetNumber;
	private String city;
	private String country;

}
