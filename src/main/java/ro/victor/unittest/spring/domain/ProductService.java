package ro.victor.unittest.spring.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.victor.unittest.spring.infra.ExternalServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.ProductDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final ExternalServiceClient externalServiceClient;
	private final ProductRepo productRepo;


	public Product getProduct(long productId) {
		Product product = productRepo.findById(productId).orElseThrow(IllegalArgumentException::new);
		boolean covidVaccineExists = externalServiceClient.covidVaccineExists();
		log.info("COVID Vaccine: " + covidVaccineExists);
		if (covidVaccineExists) {
			// heavy geo-political business logic
			throw new IllegalStateException("Bio-war");
		}
		if (!product.getSupplier().isActive()) {
			throw new IllegalStateException("Supplier inactive. Product not listed.");
		}
		return product;
	}

}
