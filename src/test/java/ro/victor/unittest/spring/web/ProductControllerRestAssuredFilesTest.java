package ro.victor.unittest.spring.web;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.victor.unittest.spring.facade.ProductFacade;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
    properties = {"label.service.base.url=http://localhost:8089"} // punctual doar pt testul asta
    )
public class ProductControllerRestAssuredFilesTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Autowired
    ProductFacade facade;
    @Test
    public void moreComplexByWireMockProgramatically() {
//        WireMock.stubFor(get(urlEqualTo("/other/thing"))
//                .willReturn(aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", "application/json")
//                        .withBody("{\"labels\":[{\"id\":12, \"value\":\"WireMock\"}]}")));

        Assertions.assertThat(facade.getLabel(12)).isEqualTo("WireMock");

    }
}
