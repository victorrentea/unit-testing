package ro.victor.unittest.spring.domain;

import org.springframework.stereotype.Service;
import ro.victor.unittest.spring.web.Happy;
import ro.victor.unittest.spring.web.Peace;

import java.time.LocalDateTime;

@Service
public class RestInPeaceService {

	public Peace getPeace(String ssn) {
		return new Peace(new Happy(ssn.toUpperCase() + " SUCCESS"));
	}

}
