package victor.testing.designhints.signatures;

import lombok.Value;

import java.time.LocalDate;

//record SprintFinishedParams(String poEmail, 2,3,4) {}
public class Precise {
   public static void sendSprintFinishedEmail(String poEmail) {
      System.out.println("Sending email to " + poEmail + " with subject 'Sprint Finished' and some body");
   }
}

@Value
class Project {
   private Long id;
   private String code;
   private String name;

   private String poEmail;
   private LocalDate startDate;
   // 10 more fields
}