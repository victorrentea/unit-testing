package ro.victor.unittest.db.search;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Job {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy = "job",cascade = CascadeType.ALL)
    private List<Skill> skills = new ArrayList<>();
    private String description;
}
