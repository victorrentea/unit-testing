package ro.victor.unittest.spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ro.victor.unittest.spring.domain.RestInPeaceService;

@RestController
public class RestInPeaceController {
	@Autowired
	private RestInPeaceService service;

	@GetMapping("/peace/{ssn}")
	public Peace peace(@PathVariable String ssn) {
		return service.getPeace(ssn);
	}
}
