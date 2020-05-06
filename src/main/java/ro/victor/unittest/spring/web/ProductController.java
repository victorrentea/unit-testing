package ro.victor.unittest.spring.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;
import ro.victor.unittest.spring.facade.ProductFacade;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
	private final ProductFacade facade;

	@GetMapping("/peace/{productId}")
	public ProductDto getProduct(@PathVariable Long productId) {
		return facade.getProduct(productId);
	}

	@PostMapping("/product/search")
	public List<ProductSearchResult> searchProduct(@RequestBody ProductSearchCriteria criteria) {
		return facade.searchProduct(criteria);
	}
}
