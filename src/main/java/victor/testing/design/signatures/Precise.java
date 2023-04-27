package victor.testing.design.signatures;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class Precise {
   public static void sendSprintFinishedEmail(String poEmail) {
      System.out.println("Sending email to " + poEmail + " with subject 'Sprint Finished' and some body");
   }
}

@Data
class Project {
   private Long id;
   //@NotNull // verificata de Hibernate la persist, sau cu @Validated pe metods
   private String code;
   private String name;

   private String poEmail;
   private LocalDate startDate;
   // 10 more fields


   public Project(String code, String name, String poEmail, LocalDate startDate) {
      this.code = Objects.requireNonNull(code);
      this.name = Objects.requireNonNull(name);
      this.poEmail = Objects.requireNonNull(poEmail);
      this.startDate = Objects.requireNonNull(startDate);
   }
}