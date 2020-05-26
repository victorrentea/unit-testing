package ro.victor.unittest.spring.web;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.repo.ProductRepo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class ProductControllerRestAssuredTest {
    @LocalServerPort
    int serverPort;
    // TODO have a look at our friend Eugen, too :
    // https://www.baeldung.com/rest-assured-tutorial

    @Before
    public void setupRestAssured() {
        RestAssured.port = serverPort;
    }

    @Autowired
    private ProductRepo productRepo;

    @Test
    public void testSearch() {
        productRepo.save(new Product()
                .setName("Cuie")
                .setCategory(Product.Category.PT_NEVASTA)
                .setSupplier(new Supplier().setName("emag")));
        RestAssured.given().auth().basic("spring","secret")
                .when()
                .body("{}")
                .contentType(ContentType.JSON)
                .post("product/search")
                .then()
                .statusCode(200)
                .log().ifError()
                .body("[0].name",Matchers.equalTo("Cuie"))
        ;
    }
//@BeforeClass
//public void startupWireMock() {
//
//}
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void moreComplexByWireMockMappingFile() {
        RestAssured.when().get("http://localhost:8089/some/thing").then()
                .statusCode(200)
                .body(Matchers.containsString("WireMock"));
    }

    @Test
    public void moreComplexByWireMockProgramatically() throws IOException {
        WireMock.stubFor(get(urlEqualTo("/other/thing"))
                .withHeader("Accept", equalTo("application/json")) // TODO uncomment to see mismatch
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
//                        .withBody("[{\"id\":12, " +
//                                "\"value\":\"WireMock\", " +
//                                "\"label\": 15" +
//                                "}]")));
                        .withBody(IOUtils.toString(
                                new ClassPathResource("response-json/test.json").getInputStream()))));
        // validates the response from wiremock - obviously, just for demo purposes; no purpose in real-life
        Response response =
                RestAssured.given().accept("application/json")
                .when().get("http://localhost:8089/other/thing")
                ;

        response.then()
//                .log().ifError()
//                .log().body()
                .time(Matchers.lessThan(1000L), TimeUnit.MILLISECONDS)
                .statusCode(200)
                .assertThat()
                .body("[0].value", CoreMatchers.equalTo("WireMock"))
        ;
    }
}
