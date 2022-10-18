package victor.testing.spring.parallel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.repo.ProductRepo;

//@Profile("!local")
//@Configuration
//class SecurityConfig {}
@Slf4j
@ActiveProfiles("db-mem")
//@DataJpaTest // test slices : porneste din Spring Boot doar parti mici : merge mai repede testu tau in clasa asta (pe local),
//  #4 dar pe CI ar putea cauza sa se creeze inca un context dedicat pt clasa asta

@SpringBootTest
            //(properties = "oProp=altaValoare") // #2 pentru ca prop nu se pot REINJECTA in beanuri, daca o prop e diferita => alt context de test
//@ActiveProfiles("prrofil") // #1 conditioneaza @Bean @Component... @Config , application-prrofil.properties
public class TestIncet {

//    @MockBean
//    private ProductRepo productRepo; // #3 @MockBean inlocuieste un bean. orice alta compo spring isi injecteaza acest repo,
    // va injecta lucruri diferite intre TestIncet si TestIncet2...
    @Test
    void unu() throws InterruptedException {
        log.info("Start1");
        Thread.sleep(1000);
        log.info("End1");
    }
    @Test
    void doi() throws InterruptedException {
        log.info("Start2");
        Thread.sleep(1000);
        log.info("End2");
    }
}
