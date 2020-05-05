package ro.victor.unittest.spring.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
//@Data NEVER
@ToString
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Category category;

    private LocalDateTime sampleDate;

    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    private Supplier supplier;

    public enum Category {
        PT_NEVASTA, PT_COPII, PT_MINE, PT_CASA
    }

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

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Product setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Product setSampleDate(LocalDateTime sampleDate) {
        this.sampleDate = sampleDate;
        return this;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Product setSupplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }
}
