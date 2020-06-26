package ro.victor.unittest.spring.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.victor.unittest.spring.infra.SafetyServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;



@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final SafetyServiceClient safetyClient;
	private final ProductRepo productRepo;

	public Product getProduct(long productId) {
		Product product = productRepo.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

		boolean safe = safetyClient.isSafe(product.getExternalRef());
		log.info("Product is safe: " + safe);
		if (!safe) {
			throw new IllegalStateException("Product is not safe: " + productId);
		}

		if (!product.getSupplier().isActive()) {
			throw new IllegalStateException("Supplier inactive. Product not listed.");
		}

		return product;
	}

}
