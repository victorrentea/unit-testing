package victor.testing.approval;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Setter(AccessLevel.NONE) // works even without setters, in case you want to enforce better entity encapsulation
public class Person {
   @Id
   private Long id;
   private String firstName;
   private String lastName;
   private LocalDate birthDate;
   @ElementCollection
   private List<String> phoneList;
}
