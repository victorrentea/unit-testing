package ro.victor.unittest.spring.facade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.infra.ExternalServiceClient;
import ro.victor.unittest.spring.web.ProductDto;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductFacadeTest {
    @MockBean
    public ExternalServiceClient externalServiceClient;

    @Autowired
    private EntityManager em;
    @Autowired
    private ProductFacade productFacade;

    @Test(expected = IllegalArgumentException.class)
    public void testWar() {
        when(externalServiceClient.covidVaccineExists()).thenReturn(true);
        productFacade.getProduct(6L);
    }

    @Test
    @Transactional
    public void testWholeWorkflow() {
        when(externalServiceClient.covidVaccineExists()).thenReturn(false);
        Product product = new Product();
        Supplier supplier = new Supplier().setActive(true);
        product.setSupplier(supplier);
        em.persist(product);
        em.persist(supplier);

        ProductDto dto = productFacade.getProduct(product.getId());
        //dto.productName
    }
}
