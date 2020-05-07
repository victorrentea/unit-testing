package ro.victor.unittest.spring.infra;

import org.springframework.stereotype.Component;

@Component
public class WhoServiceClient {

    public boolean covidVaccineExists() {
        throw new IllegalArgumentException("Can't call the external web service from this environment");
    }
}
