package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@RunWith(SpringRunner.class)

public class ProductRepoSearchTest extends AltTestBase {
	@Autowired
	private ProductRepo repo;
   

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

	@Before
	public void checkEmptyDB() {
		// delete din tabele car e te ForeignKeyaza pe tina
//	    	repo.deleteAll();
		assertThat(repo.findAll()).isEmpty();
	}
    
    
    @Test
    public void noCriteria() {
    	repo.save(new Product("A"));
    	Assert.assertEquals(1, repo.search(criteria).size());
    	assertThat(repo.search(criteria)).hasSize(1);
    }
    
    // o varianta: un test care testeaza si-si
    // daca logica e simpla (ca la noi) e suficient 1 test.
    @Test
    public void byName() {
    	repo.save(new Product("Copac"));
    	repo.save(new Product("Pom"));
    	criteria.name = "Opa";
    	List<ProductSearchResult> results = repo.search(criteria);
    	assertThat(results).hasSize(1);
    	assertThat(results.get(0).getName()).isEqualTo("Copac");
    }

    // varianta 2: doua teste separate pentur match si mismatch.
    // Preferam asta daca logica testata este foarte complexa.
    @Test
    public void byNameMatch() {
    	repo.save(new Product("Copac"));
    	criteria.name = "Copac";
    	Assertions.assertThat(repo.search(criteria)).hasSize(1);
    }
    @Test
    public void byNameMismatch() {
    	repo.save(new Product("Pom"));
    	criteria.name = "Copac";
    	List<ProductSearchResult> results = repo.search(criteria);
    	assertThat(results).isEmpty();
    }
    
    @Test
    public void bySupplier() {
    	//Given: contextul (test fixture) = aici + @Before (ev mostenite)
    	Product product = repo.save(new Product().setSupplier(supplier1));
    	repo.save(new Product().setSupplier(supplier2));
    	criteria.supplierId = supplier1.getId();
    	
    	// When - ACT
    	List<ProductSearchResult> results = repo.search(criteria);
    	
    	// Then :asserturi
    	assertThat(results).hasSize(1);
    	assertThat(results.get(0).getId()).isEqualTo(product.getId());
    }
    
    //sau, mai maniac, asa:
    @Test
    public void bySupplierGeek() {
    	repo.save(new Product().setSupplier(supplier1));

    	criteria.supplierId = supplier1.getId();
    	assertThat(repo.search(criteria)).hasSize(1);

    	criteria.supplierId = -1L;
    	assertThat(repo.search(criteria)).isEmpty();
    }


    // TODO finish

    // TODO base test class persisting supplier

    // TODO replace with composition
}

