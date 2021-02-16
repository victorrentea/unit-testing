package victor.testing.spring.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
//@Data NEVER
@ToString
public class Product {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private String name;

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    @Getter @Setter
    private ProductCategory category;

    @Getter @Setter
    private String upc;

    @Getter @Setter
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Supplier supplier;

    @Getter @Setter
    private LocalDateTime createDate;
    public Product(String name, String upc, ProductCategory category) {
        this.name = name;
        this.upc = upc;
        this.category = category;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product() {}


    public Long getId() {
        return id;
    }

}
