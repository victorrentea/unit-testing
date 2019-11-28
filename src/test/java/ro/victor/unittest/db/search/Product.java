package ro.victor.unittest.db.search;

import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@ToString
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Category category;

    @ManyToOne
    private Country originCountry;

    enum Category {
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
