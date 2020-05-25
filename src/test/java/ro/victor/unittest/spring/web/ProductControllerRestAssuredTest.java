package ro.victor.unittest.spring.web;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerRestAssuredTest {
    @LocalServerPort
    int serverPort;
    // TODO have a look at our friend Eugen, too :
    // https://www.baeldung.com/rest-assured-tutorial

    @Before
    public void setupRestAssured() {
        RestAssured.port = serverPort;
    }

    @Test
    public void test() {
        RestAssured.when().get("unsecured").then()
                .statusCode(200)
                .body(Matchers.containsString("1"));
    }

    @Test
    public void secured() {
        RestAssured.given().auth().basic("spring", "secret")
                .when().get("secured").then()
                .statusCode(200)
                .body(Matchers.containsString("99"));
    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void moreComplexByWireMockMappingFile() {
        RestAssured.when().get("http://localhost:8089/some/thing").then()
                .statusCode(200)
                .body(Matchers.containsString("WireMock"));
    }

    @Test
    public void moreComplexByWireMockProgramatically() {
        WireMock.stubFor(get(urlEqualTo("/other/thing"))
                .withHeader("Accept", equalTo("text/xml")) // TODO uncomment to see mismatch
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":12, \"value\":\"WireMock\"}]")));
        // validates the response from wiremock - obviously, just for demo purposes; no purpose in real-life
        Response response = when().get("http://localhost:8089/other/thing");

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
