package victor.testing.tools;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class ClockTestUtils {
  public static Clock fixedDateClock(String dateStr) {
    return fixedDateClock(LocalDate.parse(dateStr));
  }

  public static Clock fixedDateClock(LocalDate date) {
    Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
    return Clock.fixed(instant, ZoneId.systemDefault());
  }
}
