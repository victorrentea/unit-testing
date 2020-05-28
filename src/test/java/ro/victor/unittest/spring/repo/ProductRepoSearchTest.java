package ro.victor.unittest.spring.repo;

import com.github.tomakehurst.wiremock.extension.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Supplier;

import javax.persistence.EntityManager;

import java.util.Collections;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
@Sql
// profile, mockbean, props
public class ProductRepoSearchTest extends RepoBaseTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    protected Supplier s = new Supplier().setName("emag");

    @Before
    public void insertSupplier() {

//        log.info("Inserting supplier");
        // language=sql
//        jdbc.update("INSERT INTO SUPPLIER(ID, NAME, ACTIVE) VALUES (1,'emag', 1)", emptyMap() );
    }

    @Test
    public void test() {
        int count = jdbc.queryForObject("SELECT count(*) FROM SUPPLIER", emptyMap(), Integer.class);
        Assert.assertEquals(1, count);
    }


}