package ro.victor.unittest.db.search;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Data
@Entity
public class Job {
    @Id
    @GeneratedValue
    @Getter
    private Long id;
    private String name;
    @OneToMany(mappedBy = "job",cascade = CascadeType.ALL)
    private List<Skill> skills = new ArrayList<>();
    private String description;

    public Job setName(String name) {
        this.name = name;
        return this;
    }

    public Job addSkill(Skill skill) {
        skills.add(skill);
        skill.setJob(this);
        return this;
    }

    public List<? extends Skill> getSkills() {
        return skills;
    }
}
