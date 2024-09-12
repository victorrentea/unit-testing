package victor.testing.spring.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter @Setter
@ToString
//@Builder // nu ai voie builder daca ai si @Setter!! sau @Data
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    //generat de lombok vezi lombok.config
//    public Product setId(Long id) {
//        this.id = id;
//        return this;
//    }

    private String name;

    @Enumerated(STRING)
    private ProductCategory category;

    private String barcode;

    @ManyToOne
    private Supplier supplier;

    @CreatedDate // Spring assigns this at creation time: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing
    private LocalDate createdDate;

    @CreatedBy // Spring assigns this at creation time from SecurityContext
    private String createdBy;

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
