package victor.testing.safety;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SafetyServiceApp {
   public static void main(String[] args) {
      SpringApplication.run(SafetyServiceApp.class, "--server.port=8090");
   }

   @GetMapping
   public String home() {
      return "Try " + a("/product/1/safety") + " or " + a("/product/2/safety");
   }

   private String a(String url) {
      return "<a href=\""+url+"\">" + url + "</a>";
   }

   @GetMapping(path = "product/{id}/safety", produces = "application/json")
   public String safety(@PathVariable String id) {
      String category = "1".equals(id)?"SAFE":"UNCERTAIN";
      return "{\"entries\": [{\"category\": \"" + category + "\",\"detailsUrl\": \"http://wikipedia.com\"}]}";
   }
}
