package victor.testing.spring.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

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
