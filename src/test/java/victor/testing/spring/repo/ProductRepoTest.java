package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)  JUNIT 4

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // GRESEALA pt ca adauga ~ + 20~ sec / test
// criminal act de comis pe GIT > incetineste Jenkins. >
public class ProductRepoTest {
    @Autowired
    private ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

//    @BeforeEach
//    public final void before() {
//        repo.deleteAll();
//    }
    @Test
//    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD) // GRESEALA pt ca adauga ~ + 20~ sec / test
    public void noCriteria() {
        repo.save(new Product("A"));
        assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
    @Test
    public void noCriteriaBis() {
        repo.save(new Product("B"));
        assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }

}

