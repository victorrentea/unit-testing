package ro.victor.unittest.spring.domain;

import cucumber.api.java.en_pirate.Aye;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.victor.unittest.spring.infra.IWhoServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	@Autowired
	private IWhoServiceClient who;
	private final ProductRepo productRepo;


	public Product getProduct(long productId) {
		Product product = productRepo.findById(productId).get();
		boolean covidVaccineExists = who.covidVaccineExists();
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
