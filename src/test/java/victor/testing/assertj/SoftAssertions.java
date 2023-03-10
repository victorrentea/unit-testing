package victor.testing.assertj;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;

public class SoftAssertions {

  private record Mansion(int guests, String kitchen, String library, String candlestick) {
  }

  @Test
  void test() {
    Mansion mansion = new Mansion(7, "clean", "clean", "pristine");

    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      // TODO try to break any assertion below
      softly.assertThat(mansion.guests()).as("Living Guests").isEqualTo(7);
      softly.assertThat(mansion.kitchen()).as("Kitchen").isEqualTo("clean");
      softly.assertThat(mansion.library()).as("Library").isEqualTo("clean");
      softly.assertThat(mansion.candlestick()).as("Candlestick").isEqualTo("pristine");
    }
  }
}
