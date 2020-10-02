package victor.testing.spring.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerRestTemplateTest {
    @Autowired
    private TestRestTemplate rest; // vs RestTemplate helps with auth

    @Test
    public void testUnsecured() {
        ResponseEntity<String> result = rest.getForEntity("/unsecured", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testSecured() {
        ResponseEntity<String> result = rest.withBasicAuth("spring", "secret")
                .getForEntity("/secured", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
