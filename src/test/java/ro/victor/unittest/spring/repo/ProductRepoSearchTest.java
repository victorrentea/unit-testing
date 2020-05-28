package ro.victor.unittest.spring.repo;

import com.github.tomakehurst.wiremock.extension.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.dbunit.assertion.DbUnitAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;
import wiremock.org.checkerframework.checker.units.qual.A;

import javax.persistence.EntityManager;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
@Slf4j
@WithCommonSupplierData
// profile, mockbean, props
public class ProductRepoSearchTest extends RepoBaseTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;
//    @Autowired
//    private JdbcTemplate jdbc2;
    @Autowired
    ProductRepo repo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();
    protected Supplier s = new Supplier().setName("emag");

    @Before
    public void insertSupplier() {
//        JdbcTestUtils.deleteFromTables(jdbc2, "PRODUCT");
        jdbc.update("DELETE FROM PRODUCT",emptyMap()); // sa lasi gunoi dupa permite si debuggin
        // in baza: iei copy paste querul si-l rulezi pe baza ramasa
        // DBunit: de cereceta         new DbUnitAssert().
        assertEquals(1, (int) jdbc.queryForObject("SELECT count(*) FROM SUPPLIER", emptyMap(), Integer.class));
        assertEquals(0, (int) jdbc.queryForObject("SELECT count(*) FROM PRODUCT", emptyMap(), Integer.class));
    }

    @Test
    public void noCriteria() {
        repo.save(new Product());
        List<ProductSearchResult> results = repo.search(criteria);
        assertEquals(1, results.size());
    }

    @Test
    public void byName() {
        criteria.name = "x";
        Product product = new Product().setName("aXb");
        repo.save(product);
        List<ProductSearchResult> results = repo.search(criteria);
        assertEquals(1, results.size());
        assertEquals(product.getId(), results.get(0).getId());
        // daca verifica ca sunt 0 in tabela inainte de test,
        // devine inutil verificarea id-ului
    }
    @Test
    public void byNameNoMatch() {
        criteria.name = "y";
        repo.save(new Product().setName("x"));
        List<ProductSearchResult> results = repo.search(criteria);
        assertEquals(0, results.size());
    }


}