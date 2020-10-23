package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final SafetyClient safetyClient;
	private final ProductRepo productRepo;
	private final SupplierRepo supplierRepo;
	

	public long createProduct(ProductDto productDto) {
		boolean safe = safetyClient.isSafe(productDto.upc);
		if (!safe) {
			throw new IllegalStateException("Product is not safe: " + productDto.upc);
		}

		Product product = new Product();
		product.setName(productDto.name);
		product.setCategory(productDto.category);
		product.setUpc(productDto.upc);
		product.setSupplier(supplierRepo.getOne(productDto.supplierId));
		// TODO CR check that the supplier is active!
		product.setCreateDate(TimeProvider.currentTime());
		productRepo.save(product);
		return product.getId();
	}

	public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
		return productRepo.search(criteria);
	}

	// un prod este activ daca e creat in ultimul an
	public boolean isActive(long productId) {
		return productRepo.findById(productId).get().getCreateDate()
				.isAfter(TimeProvider.currentTime().minusYears(1));
	}
}

class TimeProvider {
	public static LocalDateTime ___OVERRIDE_TIME_ONLY_FOR_TESTS;

	public static LocalDateTime currentTime() {
		if (___OVERRIDE_TIME_ONLY_FOR_TESTS != null ) {
			return ___OVERRIDE_TIME_ONLY_FOR_TESTS;
		}
		return LocalDateTime.now();
	}
	@Deprecated
	public static void set___OVERRIDE_TIME_ONLY_FOR_TESTS(LocalDateTime testTime) {
		TimeProvider.___OVERRIDE_TIME_ONLY_FOR_TESTS = testTime;
	}
	public static void clearTestTime() {
		___OVERRIDE_TIME_ONLY_FOR_TESTS = null;
	}

}
