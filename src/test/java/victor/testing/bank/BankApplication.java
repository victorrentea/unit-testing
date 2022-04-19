package victor.testing.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.testing.bank.entity.Account;
import victor.testing.bank.entity.Client;
import victor.testing.bank.repo.ClientJpaRepository;
import victor.testing.bank.vo.ClientSearchCriteria;
import victor.testing.bank.vo.ClientSearchResult;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

//@EnableSwagger2
@SpringBootApplication
@MapperScan("ro.victor.unittest.bank.repo")
public class BankApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class);
    }

    @Autowired
    private EntityManager em;
    @Override
    @Transactional
    public void run(String... args) {
        Arrays.asList(
            new Client().setName("John Doe")
                    .setBirthDate(LocalDate.parse("2000-01-01"))
                    .add(new Account("IBAN1", Account.Type.CREDIT)),
            new Client()
                    .setName("Maria")
                    .setBirthDate(LocalDate.parse("1986-10-21"))
        ).forEach(em::persist);
    }
}


@RequiredArgsConstructor
@RestController
@Slf4j
class ClientController {
    private final ClientJpaRepository repo;

    @PostMapping("client/search")
    public List<ClientSearchResult> search(@RequestBody ClientSearchCriteria criteria) {
        log.debug("Search by criteria: " + criteria);
        List<ClientSearchResult> results = repo.search(criteria);
        log.debug("Results: " + results);
        return results;
    }
}