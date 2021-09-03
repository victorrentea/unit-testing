package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

// Unit Tests - they are super fast, don't start Spring Mockito, - these run after every commit.
// Integration Tests - Repo+DB - at every commit !!
// Component Test = apps in dockers - postgres in another docker. -- at every commit ! > 40 mins | 30 mins | 90m

// alterntive : nightly builds by using the failsafe maven plugin
@SpringBootTest//(properties = "proper=testValue")
@ActiveProfiles({"db-mem", "another-profile" })
//@Sql(scripts = "/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductRepoTest /*extends BaseRepoTest*/ { // == Integration Test
    @Autowired
    private ProductRepo repo;
    @Autowired
    CacheManager cacheManager;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @BeforeEach
    public final void before() {
//        cacheManager.getCache("aa").clear();
        repo.deleteAll();
        repo.save(new Product("A"));
    }
    @Test
    public void noCriteria() {
        // db is empty
//        repo.save(new Product("A"));
        assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
    @Test
    public void noCriteriaBis() {
//        repo.save(new Product("B"));
        assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
//    @BeforeEach
//    public final void before() {
//
//    }

}

