package ro.victor.unittest.spring.facade;

//import org.junit.Before;
//import org.junit.Test;
//import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.infra.ExternalServiceClient;
import ro.victor.unittest.spring.web.ProductDto;

import javax.persistence.EntityManager;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class ProductFacadeTest extends AbstractTransactionalTestNGSpringContextTests {
    @MockBean
    public ExternalServiceClient externalServiceClient;

    @Autowired
    private EntityManager em;
    @Autowired
    private ProductFacade productFacade;

    @MockBean
    private Clock clock;

    private LocalDateTime currentTime = LocalDateTime.now();

    @BeforeClass
    public void setupTime() {
        when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test(expectedExceptions = IllegalStateException.class)
    public void testWar() {
        when(externalServiceClient.covidVaccineExists()).thenReturn(true);
        Product product = new Product();
        em.persist(product);
        productFacade.getProduct(product.getId());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void testWholeWorkflow() {
        when(externalServiceClient.covidVaccineExists()).thenReturn(false);
        Product product = new Product().setName("Prod");
        Supplier supplier = new Supplier().setActive(true);
        product.setSupplier(supplier);
        em.persist(product);
        em.persist(supplier);
        currentTime = LocalDateTime.parse("2020-01-01T20:00:00");

        ProductDto dto = productFacade.getProduct(product.getId());

        assertThat(dto.productName).isEqualTo("Prod");
        System.out.println(dto.sampleDate);
        assertThat(dto.sampleDate).isEqualTo("2020-01-01");
    }
}
