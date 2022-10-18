package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;

import static java.time.LocalDateTime.now;


// Vlad: dupa fiecare @Test sa fac ROLLBACK la tranzactia pe care am inserat date in DB


public class TestNesamtzat extends AbstractInMemDBTest {
   @MockBean
   public SafetyClient safetyClientMock;
   @Autowired
   private ProductRepo productRepo;


   @Test
   public void testCeLasaDupaElGunoi() {
      // GIVEN
      productRepo.save(new Product());

   }


}
