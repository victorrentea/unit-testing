package ro.victor.unittest.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.repo.ProductRepo;

import java.util.stream.Stream;

@Component
public class DummyDataCreator implements CommandLineRunner {
   private final ProductRepo productRepo;

   public DummyDataCreator(ProductRepo productRepo) {
      this.productRepo = productRepo;
   }

   @Override
   public void run(String... args) throws Exception {
      Stream.of("Apple","Pear","Watermelon", "Strawberry")
          .map(Product::new)
          .forEach(productRepo::save);
   }
}
