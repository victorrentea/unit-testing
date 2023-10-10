package victor.testing.spring.product.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity // Victor nu implem equals pe entitati
@Getter @Setter
@ToString
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private ProductCategory category;

    private String sku;

    @ManyToOne
    private Supplier supplier;

    @CreatedDate // Spring assigns this at creation time: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing
    private LocalDate createdDate;

    @CreatedBy // Spring assigns this at creation time from SecurityContext
    private String createdBy;

    @LastModifiedDate
    // Spring assigns this at creation time: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing
    private LocalDate modifiedDate;

    @LastModifiedBy // Spring assigns this at creation time from SecurityContext
    private String modifiedBy;

    public Product(String name, String sku, ProductCategory category) {
        this.name = name;
        this.sku = sku;
        this.category = category;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product() {}


}
