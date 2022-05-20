//package victor.testing.assertj;
//
//import org.assertj.core.api.AutoCloseableSoftAssertions;
//import org.junit.jupiter.api.Test;
//
//public class SoftAssertions {
//
//    // single assert rule = one test should only one aspect of the feature
//    //  - should only do one assert (WRONG)
//
//   @Test
//   void test() {
//      try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
////          softly.assertThatThrownBy(() -> verify(mock).f()).doesNotThrowAnyException();
////          softly.assertThatThrownBy(() -> verify(mock).g()).doesNotThrowAnyException();
////          softly.assertThatThrownBy(() -> verify(mock).h()).doesNotThrowAnyException();
//         softly.assertThat(mansion.guests()).as("Living Guests").isEqualTo(7);
//         softly.assertThat(mansion.kitchen()).as("Kitchen").isEqualTo("clean");
//         softly.assertThat(mansion.library()).as("Library").isEqualTo("clean");
//         softly.assertThat(mansion.revolverAmmo()).as("Revolver Ammo").isEqualTo(6);
//         softly.assertThat(mansion.candlestick()).as("Candlestick").isEqualTo("pristine");
//         softly.assertThat(mansion.colonel()).as("Colonel").isEqualTo("well kempt");
//         softly.assertThat(mansion.professor()).as("Professor").isEqualTo("well kempt");
//         // no need to call assertAll, it is done when softly is closed.
//      }
//   }
//}
