package ro.victor.unittest.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class SpringApp {
}

@Service
class MyService {
    @Autowired
    private LdapUserRepo repo;

    public String logic(long id) {
        return repo.getById(id).getName();
    }
}

class User {
    private final String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
@Repository
class LdapUserRepo {

    public User getById(long id) {
        throw new RuntimeException("Should not call from unit test pt ca ma duc in baza de date ");
    }
}

