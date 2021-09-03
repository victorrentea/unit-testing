//package victor.testing.spring.repo;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import victor.testing.spring.domain.Product;
//import victor.testing.spring.web.dto.ProductSearchCriteria;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//// Unit Tests - they are super fast, don't start Spring Mockito, - these run after every commit.
//// Integration Tests - Repo+DB - at every commit !!
//// Component Test = apps in dockers - postgres in another docker. -- at every commit ! > 40 mins | 30 mins | 90m
//
//// alterntive : nightly builds by using the failsafe maven plugin
//@SpringBootTest
//@ActiveProfiles("db-mem")
////@Sql(scripts = "/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
//@Transactional
//
//public class ProductRepoTest2 /*extends BaseRepoTest*/ { // == Integration Test
//    @Autowired
//    private ProductRepo repo;
//
//    private ProductSearchCriteria criteria = new ProductSearchCriteria();
//
//    @BeforeEach
//    public final void before() {
//        repo.save(new Product("A"));
//    }
//    @Test
//    public void noCriteria() {
//        // db is empty
////        repo.save(new Product("A"));
//        assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
//    }
//    @Test
//    public void noCriteriaBis() {
////        repo.save(new Product("B"));
//        assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
//    }
////    @BeforeEach
////    public final void before() {
////
////    }
//
//}
//
