package victor.testing.design.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class TimeUtils {
  static Clock fixedClock(LocalDate date) {
     Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
     return Clock.fixed(instant, ZoneId.systemDefault());
  }
}
