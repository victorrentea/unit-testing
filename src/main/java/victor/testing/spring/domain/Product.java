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
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Category category;

    @Setter
    private LocalDateTime sampleDate;
    @Getter @Setter
    private String externalRef;

    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Getter @Setter
    private Supplier supplier;

    public enum Category {
        WIFE, KIDS, ME, HOME
    }
    public Product(String name) {
        this.name = name;
    }
    public Product() {}


    public Optional<LocalDateTime> getSampleDate() {
        return Optional.ofNullable(sampleDate);
    }

    public Long getId() {
        return id;
    }

    public Product setId(Long id) {
        this.id = id;
        return this;
    }

}
