package ro.victor.unittest.spring.infra;

import org.springframework.stereotype.Component;

@Component
public class WhoServiceClient {
    public boolean covidVaccineExists() {
        // cod care cheam WS extern
        throw new IllegalArgumentException("Esti in teste frate, n-i cum sa chemi Servicii Externe");
    }
}
