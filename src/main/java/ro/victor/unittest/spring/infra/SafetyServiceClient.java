package ro.victor.unittest.spring.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.List;
@Slf4j
@Component
public class SafetyServiceClient {

    @Value("${safety.service.url.base}")
    private URL baseUrl;

    @Autowired
    private RestTemplate rest;

    public boolean isSafe(String externalRef) {
        ResponseEntity<List<SafetyReportDto>> response = rest.exchange(
            baseUrl.toString() + "/product/{id}/safety",
            HttpMethod.GET, null,
            new ParameterizedTypeReference<List<SafetyReportDto>>() { },
            externalRef
            );

        if (response.getBody() == null) {
            log.warn("No body received!?");
            return false;
        }
        return response.getBody().stream()
            .allMatch(report -> report.isSafeToSell() && report.getCategory().equals("DETERMIND")); // BUG HERE
    }
}
