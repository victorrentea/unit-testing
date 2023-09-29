package victor.testing.spring.product.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
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
    private LocalDate createDate;

    @CreatedBy// extrage din SecurityContextHolder userul curent
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updateDate;

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
