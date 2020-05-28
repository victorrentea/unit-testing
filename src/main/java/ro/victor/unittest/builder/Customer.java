package ro.victor.unittest.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static java.util.Collections.unmodifiableList;

public class Customer {

	@Setter
	private String name;
	@Getter
	@Setter
	private String phone;
	private List<String> labels = new ArrayList<>();
	@Getter @Setter
	private Address address;
	@Getter @Setter
	private Date createDate;

	public String getName() {
		return name;
	}




	public List<String> getLabels() {
		return unmodifiableList(labels);
	}

	public void addLabels(String... labels) {
		this.labels.addAll(Arrays.asList(labels));
	}


}
