package ro.victor.unittest.spring.facade;

//import org.junit.Before;
//import org.junit.Test;
//import org.junit.Test;

import org.junit.Before;
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
// DONE

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@ActiveProfiles("db-mem")
public class ProductFacadeMockClientTest {
    @MockBean
    public SafetyServiceClient mockSafetyClient;

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductFacade productFacade;
    @MockBean
    private Clock clock;

    private LocalDateTime currentTime = LocalDateTime.now();

    @Before
    public void setupTime() {
        // callback not thenReturn because I want it to lazy eval durin prod call, so that I can change the field value in @Test bellow (ruunig after this @Before line)
        when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Test(expected = IllegalStateException.class)
    public void throwsForUnsafeProduct() {
        Product product = new Product().setExternalRef("EXTREF");
        productRepo.save(product);
        when(mockSafetyClient.isSafe("EXTREF")).thenReturn(false);
        productFacade.getProduct(product.getId());
    }

    @Test
    public void fullOk() {
        Product product = new Product().setExternalRef("EXTREF").setName("Prod");
        Supplier supplier = new Supplier().setActive(true);
        product.setSupplier(supplier);
        productRepo.save(product);
        when(mockSafetyClient.isSafe("EXTREF")).thenReturn(true);
        currentTime = LocalDateTime.parse("2020-01-01T20:00:00");

        ProductDto dto = productFacade.getProduct(product.getId());

        assertThat(dto.productName).isEqualTo("Prod");
        System.out.println(dto.sampleDate);
        assertThat(dto.sampleDate).startsWith("2020-01-01T19:59:55");
    }

    // TIP: sample response from Safety Service: "[{\"category\":\"DETERMINED\", \"safeToSell\":true}]"

    // TIP2:
    // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
    // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
