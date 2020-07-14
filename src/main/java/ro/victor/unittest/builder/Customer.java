package ro.victor.unittest.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static java.util.Collections.unmodifiableList;

public class Customer {

	@Getter
	@Setter
	private String name;
	private String phone;
	private List<String> labels = new ArrayList<>();
	@Getter
	@Setter
	private Address address;
	@Getter
	@Setter
	private Date createDate;

	public List<String> getLabels() {
		return unmodifiableList(labels);
	}
	public Customer addLabels(String... newLabels) { // pt teste
		labels.addAll(Arrays.asList(newLabels));
		return this;
	}
}
