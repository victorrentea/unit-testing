package ro.victor.unittest.spring.facade;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.victor.unittest.spring.infra.IWhoServiceClient;

@Component
//@Profile("test") // poate fi evitata
@Profile("no-who-client")
@Primary // asta inseamna ca oricand are de ales intre implem reala din src/main/java si asta, o ia pe asta
public class WhoServiceClientForTests implements IWhoServiceClient {
   private boolean vaccineExists = false;

   public void setVaccineExists(boolean vaccineExists) {
      this.vaccineExists = vaccineExists;
   }

   @Override
   public boolean covidVaccineExists() {
      return vaccineExists;
   }
}
