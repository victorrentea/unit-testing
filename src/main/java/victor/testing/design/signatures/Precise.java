package victor.testing.design.signatures;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
public class Precise {
   public static void sendSprintFinishedEmail(String poEmail) {
      log.info("Sending email to " +
                         poEmail + " " +
                         "with subject 'Sprint Finished' and some body");
   }
}

@Data
class Project {
   private Long id;
   private String code;
   private String name;

   private String poEmail;
   private LocalDate startDate;
   // 10 more fields

   public Project(String code, String name) {
      this.code = Objects.requireNonNull(code);
      this.name = Objects.requireNonNull(name);
   }
}