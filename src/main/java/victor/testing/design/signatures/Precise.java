package victor.testing.design.signatures;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

public class Precise {
   public static void sendSprintFinishedEmail(Project project) {
      System.out.println("Sending email to " + project.getPoEmail() +
                         " with subject 'Sprint Finished' and some body");
   }
}

@Data
@Entity
class Project {
   @Id
   private Long id;
   private String code;
   private String name;

   private String poEmail;
   private LocalDate startDate;
   // 10 more fields

}