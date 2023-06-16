package victor.testing.spring.product.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@ToString
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private ProductCategory category;

    private String barcode;

    @ManyToOne
    private Supplier supplier;

//    @CreatedBy // iti pune in campul asta  userul din SecurityCOntextHolder....
//    private String createdBy;

    @CreatedDate // Spring assigns this at creation time: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing
    private LocalDate createDate;

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
