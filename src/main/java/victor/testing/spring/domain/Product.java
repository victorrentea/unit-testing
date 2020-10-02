package victor.testing.spring.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
//@Data NEVER
@ToString
public class Product {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private ProductCategory category;

    @Getter @Setter
    private String upc;

    @Getter @Setter
    @ManyToOne
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
