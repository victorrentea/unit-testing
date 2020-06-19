package ro.victor.unittest.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.repo.ProductRepo;

@Component
@Profile("!test")
public class DummyDataInserter implements CommandLineRunner {
    @Autowired
    private ProductRepo productRepo;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        productRepo.save(new Product("Scaun la cap"));
    }

}
