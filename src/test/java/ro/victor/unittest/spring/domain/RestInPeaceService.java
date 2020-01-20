package ro.victor.unittest.spring.domain;

import org.springframework.stereotype.Service;
import ro.victor.unittest.spring.web.Peace;

@Service
public class RestInPeaceService {

	public Peace getPeace(String ssn) {
		return new Peace(ssn.toUpperCase());
	}

}
