package ro.victor.unittest.db.search;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Skill {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    private Job job;

    public String getName() {
        return name;
    }

    void setJob(Job job) {
        this.job = job;
    }

    public Skill(String name) {
        this.name = name;
    }
}
