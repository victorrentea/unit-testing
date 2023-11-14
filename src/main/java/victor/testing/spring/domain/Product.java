package victor.testing.spring.domain;

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

    private String upc;

    @ManyToOne
    private Supplier supplier;

    @CreatedDate // Spring assigns this at creation time: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing
    private LocalDate createdDate;

    @CreatedBy // Spring assigns this at creation time from SecurityContext
    private String createdBy; // fitza, magie

    public Product(String name, String upc, ProductCategory category) {
        this.name = name;
        this.upc = upc;
        this.category = category;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product() {}


}
