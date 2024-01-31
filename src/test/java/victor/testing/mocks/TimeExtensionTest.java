package victor.testing.mocks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeExtensionTest {
  @RegisterExtension
  public TimeExtension timeExtension = new TimeExtension("2019-09-29");

  @Test
  public void mockStaticTime() {
    // when (tested code)
    String actual = "Current date is " + LocalDate.now();

    // then
    System.out.println(actual);
    assertThat(actual).isEqualTo("Current date is 2019-09-29");
  }

}
