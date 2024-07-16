package victor.testing.design.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class ClockUtils {
  public static Clock fixedClock(String dateStr) {
    return fixedClock(LocalDate.parse(dateStr));
  }

  public static Clock fixedClock(LocalDate date) {
    Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
    return Clock.fixed(instant, ZoneId.systemDefault());
  }
}
