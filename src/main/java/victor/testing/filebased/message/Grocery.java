package victor.testing.filebased.message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Grocery {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int price;

    public Grocery(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
