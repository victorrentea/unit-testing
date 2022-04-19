package victor.testing.spring.repo.sub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest//(properties = "safety.client.url=localhost:port/mockserver/in/docker")
@Transactional
@ActiveProfiles({"db-mem", "in-mem-kafka"})
public class ProductRepo2Test {
   @Autowired
   ProductRepo repo;

   @MockBean
   SafetyClient safetyClient;

   ProductSearchCriteria criteria = new ProductSearchCriteria();

   @Test
   public void noCriteria() {
      repo.save(new Product("A"));
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1);
   }
}

