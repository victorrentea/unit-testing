package ro.victor.unittest.spring.infra;

import org.springframework.stereotype.Component;

@Component
public class WhoServiceClient implements IWhoServiceClient {
    @Override
    public boolean covidVaccineExists() {
        throw new IllegalArgumentException("Esti in teste frate, n-i cum sa chemi AUthorizatioBService");
    }
}
