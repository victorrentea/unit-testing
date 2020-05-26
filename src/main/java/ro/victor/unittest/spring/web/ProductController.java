package ro.victor.unittest.spring.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.victor.unittest.spring.facade.ProductFacade;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductFacade facade;

    @PostMapping("product/search")
    public List<ProductSearchResult> search(@RequestBody ProductSearchCriteria criteria) {
        return facade.searchProduct(criteria);
    }

    @GetMapping("unsecured")
    public int unsecured() {
        return 1; // dummy endpoint to test security
    }

    @GetMapping("secured")
    public int secured() {
        return 99; // dummy endpoint to test security
    }
    @GetMapping("secured/method")
    public int securedByAnnotation() {
        return 99; // dummy endpoint to test security
    }

}
