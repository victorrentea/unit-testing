package ro.victor.unittest.spring.facade;

//import org.junit.Before;
//import org.junit.Test;
//import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.infra.SafetyServiceClient;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.ProductDto;

import javax.persistence.EntityManager;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9988")
@ActiveProfiles("db-mem")

public class ProductFacadeTest {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductFacade productFacade;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9988);

    @Test(expected = IllegalStateException.class)
    public void test() {
        Product product = new Product().setExternalRef("UNSAFE-REF");
        productRepo.save(product);
        productFacade.getProduct(product.getId());
    }
}





    // TIP1:
    // WireMock.stubFor(get(urlEqualTo("/product/EXTREF/safety")).willReturn(aResponse()...)));
    // "[{\"category\":\"DETERMINED\", \"safeToSell\":true}]"


    // TIP2:
    // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
    // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
