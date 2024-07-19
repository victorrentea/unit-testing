package victor.testing.spring.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Data
public class Supplier {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private String name;
    private boolean active;
}
