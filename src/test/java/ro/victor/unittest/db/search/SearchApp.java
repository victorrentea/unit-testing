package ro.victor.unittest.db.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SearchApp implements CommandLineRunner{

    @Autowired
    private JobRepository repo;

    public static void main(String[] args) {
        SpringApplication.run(SearchApp.class);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(repo);
    }
}
