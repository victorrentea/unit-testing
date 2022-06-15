package victor.testing.spring.service.subpachet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:") // acest url ii zice bazei de date H2 sa starteze o baza in memoria procesului care vrea sa se conecteze la ea.
public class UnTestGolDarSuperUtil {
    @Test
    void golDarVerificaCaSpringLeagaBineDep_caEntHibernateSuntCorecte_caLiquibaseScriptsNuSeBatIntreEle() {
// caching properties
    }
}
