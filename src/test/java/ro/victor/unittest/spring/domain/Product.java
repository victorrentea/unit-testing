package ro.victor.unittest.spring.domain;

import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@ToString
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Category category;

    private LocalDate creationDate;

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Product setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @ManyToOne
    private Country originCountry;

    public enum Category {
        CASA, MASINA, COPII, NEVASTA
    }

    public Long getId() {
        return id;
    }

    public Country getOriginCountry() {
        return originCountry;
    }

    public Product setOriginCountry(Country originCountry) {
        this.originCountry = originCountry;
        return this;
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
}
