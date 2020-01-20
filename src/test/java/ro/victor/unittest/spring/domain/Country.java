package ro.victor.unittest.spring.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Country {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Country setName(String name) {
        this.name = name;
        return this;
    }
}
