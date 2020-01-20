package ro.victor.unittest.spring.infra;

import org.springframework.stereotype.Component;

@Component
public class ExternalServiceClient {

    public String callService() {
        throw new IllegalArgumentException("Esti in teste frate, n-i cum sa chemi AUthorizatioBService");
    }
}
