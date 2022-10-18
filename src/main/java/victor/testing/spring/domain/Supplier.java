package victor.testing.spring.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Supplier {

    @Id
    @GeneratedValue
    @Getter @Setter
    private Long id;
    @Getter @Setter
    @Column(nullable = false)
    private String name;

    @Getter @Setter
    private boolean active;

    public Supplier() {}

    public Supplier(String name) {
        this.name = name;
    }

}
