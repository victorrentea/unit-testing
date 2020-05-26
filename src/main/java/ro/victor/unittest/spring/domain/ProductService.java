package ro.victor.unittest.spring.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.victor.unittest.spring.infra.WhoServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final WhoServiceClient whoServiceClient;
	private final ProductRepo productRepo;


	public Product getProduct(long productId) {
		Product product = productRepo.findById(productId).get();
		boolean covidVaccineExists = whoServiceClient.covidVaccineExists(); //WS care nu poate fi apelat din teste
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
