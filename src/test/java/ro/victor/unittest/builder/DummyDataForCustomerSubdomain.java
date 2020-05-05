package ro.victor.unittest.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;

// Object Mother F**ker
public class DummyDataForCustomerSubdomain {
    public static Customer aValidCustomer() {
        return new Customer()
                .setName("John")
                .setLabels(Arrays.asList("appender", "b"))
                .setAddress(new Address().setCity("Bucharest"));
    }
}

@Component
class DummyDataPersister implements CommandLineRunner {
    @Autowired
    EntityManager em;

    @Transactional
    public void run(String... args) throws Exception {
//        em.persist(new User());

    }
}
