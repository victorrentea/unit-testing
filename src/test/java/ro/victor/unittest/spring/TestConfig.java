package ro.victor.unittest.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
public class TestConfig {

    @Bean
    @Profile("test")
    public Clock clock(@Value("${current.date}") String currentDate) {
        System.out.println("parsing " + currentDate);
        Instant instant = LocalDate.parse(currentDate).atStartOfDay(ZoneId.systemDefault()).toInstant();

        return Clock.fixed(instant, ZoneId.systemDefault());
    }
}
