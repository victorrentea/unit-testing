package victor.testing.builder;

import lombok.Data;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class Customer {

	private String name;
	private String phone;
	private List<String> labels = new ArrayList<>();
	private Address address;
	private Date createDate;

	public Customer addLabels(String... labelsArr) {
		labels.addAll(Arrays.asList(labelsArr));
		return this;
	}
}
