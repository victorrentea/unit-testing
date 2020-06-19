package ro.victor.unittest.spring.facade;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ro.victor.unittest.spring.infra.IWhoServiceClient;

@Component
@Primary // asta inseamna ca oricand are de ales intre implem reala din src/main/java si asta, o ia pe asta
public class WhoServiceClientForTests implements IWhoServiceClient {
   public static boolean vaccineExists = false;

   @Override
   public boolean covidVaccineExists() {
      return vaccineExists;
   }
}
