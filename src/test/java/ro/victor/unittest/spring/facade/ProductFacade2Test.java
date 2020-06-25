package ro.victor.unittest.spring.facade;

//import org.junit.Before;
//import org.junit.Test;
//import org.junit.Test;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.infra.SafetyServiceClient;
import ro.victor.unittest.spring.web.ProductDto;

import javax.persistence.EntityManager;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(properties = "safety.service.url.base=http://localhost:8089")
public class ProductFacade2Test {
    @Autowired
    private EntityManager em;
    @Autowired
    private ProductFacade productFacade;

    @MockBean
    private Clock clock;

    private LocalDateTime currentTime = LocalDateTime.now();

    @Before
    public void setupTime() {
        when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test(expected = IllegalStateException.class)
    public void throwsWhenNotSafe() {
        Product product = new Product().setSupplier(new Supplier().setActive(true));

        em.persist(product);

        WireMock.stubFor(get(urlEqualTo("/product/"+product.getId()+"/safety"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[{\"category\":\"DETERMINED\", \"safeToSell\":false}]")));
        productFacade.getProduct(product.getId());
    }

    @Test
    public void success() {
        Product product = new Product().setName("Prod");
        Supplier supplier = new Supplier().setActive(true);
        product.setSupplier(supplier);
        em.persist(product);
//        em.persist(supplier);
        currentTime = LocalDateTime.parse("2020-01-01T20:00:00");

        WireMock.stubFor(get(urlEqualTo("/product/"+product.getId()+"/safety"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[{\"category\":\"DETERMINED\", \"safeToSell\":true}]")));

        ProductDto dto = productFacade.getProduct(product.getId());

        assertThat(dto.productName).isEqualTo("Prod");
        System.out.println(dto.sampleDate);
        assertThat(dto.sampleDate).isEqualTo("2020-01-01");
    }
}
