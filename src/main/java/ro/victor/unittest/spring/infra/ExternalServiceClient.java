package ro.victor.unittest.spring.infra;

import org.springframework.stereotype.Component;

@Component
public class ExternalServiceClient {

    public boolean covidVaccineExists() {
        throw new IllegalArgumentException("Esti in teste frate, n-ai cum sa chemi AUthorizatioBService");
    }
}
