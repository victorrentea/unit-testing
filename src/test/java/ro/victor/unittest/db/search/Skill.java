package ro.victor.unittest.db.search;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Skill {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    private Job job;

    public Skill(String name) {
        this.name = name;
    }
}
