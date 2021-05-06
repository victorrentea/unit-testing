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
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private ProductCategory category;

    @Getter @Setter
    private String barcode;

    @Getter @Setter
    @ManyToOne//(cascade = CascadeType.PERSIST)
    private Supplier supplier;

    @Getter @Setter
    private LocalDateTime createDate;
    public Product(String name, String barcode, ProductCategory category) {
        this.name = name;
        this.barcode = barcode;
        this.category = category;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product() {}


}
