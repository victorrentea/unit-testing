package victor.testing.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Supplier {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    @Column(unique = true)
    private String name;
    private boolean active;
}
