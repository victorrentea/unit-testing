package victor.testing.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;

import java.util.stream.Stream;

@Component
@Profile("insertDummyData")
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
