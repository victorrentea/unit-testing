package ro.victor.unittest.spring.infra;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
//@Profile("!test") // de evitat. pt ca daca pui aialalta implem in src/test/java, va fi vizibila doar din teste (DUH!)
public class WhoServiceClient implements IWhoServiceClient {
    @Override
    public boolean covidVaccineExists() {
        throw new IllegalArgumentException("Esti in teste frate, n-i cum sa chemi AUthorizatioBService");
    }
}
