package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.infra.ExternalServiceClient;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

// profile, mockbean, props
public class ProductRepoSearchTest extends RepoBaseTest{

    // TODO assert empty

    // TODO test name, test category
}

