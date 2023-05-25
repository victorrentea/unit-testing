package victor.testing.design.signatures;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

public class Precise {
   // happier prod
   public static void sendSprintFinishedEmail(String poEmail) {
      System.out.println("Sending email to " + poEmail +
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