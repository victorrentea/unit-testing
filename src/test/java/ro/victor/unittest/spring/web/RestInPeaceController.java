package ro.victor.unittest.spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;
import ro.victor.unittest.spring.facade.SomeFacade;

import java.util.List;

@RestController
public class RestInPeaceController {
	@Autowired
	private SomeFacade facade;

	@GetMapping("/peace/{ssn}")
	public Peace peace(@PathVariable String ssn) {
		return facade.getPeace(ssn);
	}

	@PostMapping("/product/search")
	public List<ProductSearchResult> searchProduct(@RequestBody ProductSearchCriteria criteria) {
		return facade.searchProduct(criteria);
	}
}
