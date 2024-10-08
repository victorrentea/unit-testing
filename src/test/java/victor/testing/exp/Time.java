package victor.testing.exp;

import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import victor.testing.tools.TimeExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodName.class)
  public class Time {
    @RegisterExtension
    TimeExtension timeExtension = new TimeExtension("2023-12-25");

    @Test
    public void fixedTime() {
      // when
      LocalDate today = testedCode();

      assertThat(today).isEqualTo("2023-12-25");
    }

    @Test
    public void fixedTime2() {
      timeExtension.setDate(LocalDate.parse("2023-12-24"));
      // when
      LocalDate today = testedCode();

      assertThat(today).isEqualTo("2023-12-24");
    }

    private static LocalDate testedCode() { // deep in a dark library
      return LocalDate.now();
    }
  }