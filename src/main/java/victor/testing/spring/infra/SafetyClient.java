package victor.testing.spring.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class SafetyClient {

    private final RestTemplate rest;
    @Value("${safety.service.url.base}")
    private URL baseUrl;

    public boolean isSafe(String barcode) {
        ResponseEntity<SafetyReportDto> response = rest.getForEntity(
            baseUrl + "/product/{barcode}/safety",
            SafetyReportDto.class, barcode);

        boolean safe = response.getBody().getEntries().stream()
            .anyMatch(this::entryIsSafe);
        log.info("Product is safe: " + safe);
        return safe;
    }

    private boolean entryIsSafe(SafetyEntryDto report) {
        renamePesteTot();
        return "SAFE".equals(report.getCategory());

    }
    private void renamePesteTot() {

    }

}
