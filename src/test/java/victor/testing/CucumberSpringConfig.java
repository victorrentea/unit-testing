package victor.testing;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//@CucumberContextConfiguration //  from io.cucumber:cucumber-spring:7.0.0
@SpringBootTest//(classes = CucumberTennisContext.class)
@ActiveProfiles("test")
public class CucumberSpringConfig {

}
