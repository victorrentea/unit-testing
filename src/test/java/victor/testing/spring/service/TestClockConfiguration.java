package victor.testing.spring.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;

import static victor.testing.tools.ClockTestUtils.fixedDateClock;

@Configuration
class TestClockConfiguration {
    @Bean
    @Primary // takes priority over the production Clock bean
    public Clock tclock() { // replaces the bean in /src/main SPRING CONTEXT
      return fixedDateClock("2025-12-25");
    }
  }