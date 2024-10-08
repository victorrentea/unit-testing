package victor.testing.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.testing.api.dto.ProductDto;
import victor.testing.api.dto.ProductSearchCriteria;
import victor.testing.api.dto.ProductSearchResult;

import java.util.List;

@RestController
public class ProductApi {
  @PostMapping("product/create")
  void create(@RequestBody @Validated ProductDto productDto) {
    // stub
  }

  @PostMapping("product/search")
  List<ProductSearchResult> search(@RequestBody ProductSearchCriteria criteria) {
    return null; // stub
  }

  @GetMapping("product/{id}")
  ProductDto get(@PathVariable long id) {
    return null;// stub
  }

  @DeleteMapping("product/{id}")
  void delete(@PathVariable long id) {
    // stub
  }

  @PostMapping("product/export")
  String startExport() {
    return null;// stub
  }
}
