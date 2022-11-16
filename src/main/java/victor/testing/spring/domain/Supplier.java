package victor.testing.spring.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
public class Supplier {

    @Id
    @GeneratedValue
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String name;

    @Getter @Setter
    private boolean active;

//    @OneToMany(cascade = CascadeType.ALL)
    public Supplier() {}

    public Supplier(String name) {
        this.name = name;
    }

}
