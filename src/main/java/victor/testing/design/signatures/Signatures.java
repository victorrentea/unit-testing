package victor.testing.design.signatures;

import lombok.Data;

import java.time.LocalDate;

public class Signatures {
   public static void sendSprintFinishedEmail(String poEmail) {
      System.out.println("Sending email to " + poEmail +
          " with subject 'Sprint Finished' ... ");
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
}