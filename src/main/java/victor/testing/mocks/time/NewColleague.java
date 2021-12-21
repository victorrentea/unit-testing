package victor.testing.mocks.time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class NewColleague {
   private final TimeBasedLogic timeBasedLogic;

   public void method() {
      timeBasedLogic.isFrequentBuyer(1, LocalDate.now());
   }
}
