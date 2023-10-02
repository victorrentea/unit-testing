package victor.testing.design.purity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


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

    public Supplier() {}

    public Supplier(String name) {
        this.name = name;
    }

}
