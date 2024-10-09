package victor.testing;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.tennis.CucumberTennisContext;

@CucumberContextConfiguration //  from io.cucumber:cucumber-spring:7.0.0
@SpringBootTest//(classes = CucumberTennisContext.class)
@ActiveProfiles("test")
public class CucumberSpringConfig {

}
