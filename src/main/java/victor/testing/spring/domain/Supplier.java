package victor.testing.spring.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Supplier {

    @Id
    @GeneratedValue
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private String vatCode;

    @Getter
    private boolean active;

    public Supplier() {}

    public Supplier(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Supplier setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Supplier setName(String name) {
        this.name = name;
        return this;
    }

    public String getVatCode() {
        return vatCode;
    }

    public Supplier setVatCode(String vatCode) {
        this.vatCode = vatCode;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Supplier setActive(boolean active) {
        this.active = active;
        return this;
    }
}
