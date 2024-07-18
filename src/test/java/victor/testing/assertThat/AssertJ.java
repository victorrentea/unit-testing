package victor.testing.assertThat;


import lombok.NonNull;
import lombok.Value;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftlyExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("on demand - failures are fun")
public class AssertJ {
  // automatically via dependency spring-boot-test or manually via org.assertj:assertj-core

  @Nested
  @TestMethodOrder(MethodName.class)
  public class CollectionsSimple {
    @Test
    public void size1_JUnit() {
      assertEquals(1, testedMethod().size());
    }

    @Test
    public void size1_AssertJ() {
      assertThat(testedMethod()).hasSize(1);
    }

    @Test
    public void onceInAnyOrder_JUnit() {
      assertTrue(testedMethod().containsAll(List.of(100, 200, 700)));
      assertEquals(3, testedMethod().size());
    }

    @Test
    public void onceInAnyOrder_AssertJ() {
      assertThat(testedMethod()).containsExactlyInAnyOrder(100, 200, 300);
    }

    @Test
    public void contains_JUnit() {
      assertTrue(testedMethod().containsAll(List.of(100, 200)));
      assertFalse(testedMethod().contains(500));
    }

    @Test
    public void contains_AssertJ() {
      assertThat(testedMethod())
          .contains(100, 200)
          .doesNotContain(500);
    }

    private List<Integer> testedMethod() {
      return List.of(100, 200, 300, 300);
    }
  }

  @Nested
  @TestMethodOrder(MethodName.class)
  public class CollectionsElements {

    private final List<Character> fellowship = List.of(
        new Character("Frodo", 20, new Race("Hobbit")),
        new Character("Sam", 18, new Race("Hobbit")),
        new Character("Legolas", 1000, new Race("Elf")),
        new Character("Boromir", 37, new Race("Man")),
        new Character("Gandalf the Gray", 120, new Race("Man")),
        new Character("Aragorn", 39, new Race("Man")),
        new Character("Gimli", 40, new Race("Dwarf"))
    );

    @Test
    public void attribute_oneOf_JUnit() {
      // preprocess the collection before the assertion:
      Set<String> races = fellowship.stream()
          .map(Character::race)
          .map(Race::name)
          .collect(toSet());
      assertEquals(Set.of("Man", "Dwarf", "Elf", "Hobbit"), races);
    }

    @Test
    public void attribute_oneOf_AssertJ() {
      assertThat(fellowship)
          .map(Character::race)
          .map(Race::name)
          .containsOnly("Man", "Dwarf", "Elf", "Hobbit");
    }

    @Test
    public void elementsWithProperties_JUnit() {
      assertTrue(fellowship.stream().anyMatch(c -> c.name().equals("Frodo") && c.race().name().equals("Hobbit")));
      assertTrue(fellowship.stream().anyMatch(c -> c.name().equals("Aragorn") && c.race().name().equals("Man")));
      assertTrue(fellowship.stream().anyMatch(c -> c.name().equals("Legolas") && c.race().name().equals("Elf")));
    }

    @Test
    public void elementsWithProperties_AssertJ() {
      assertThat(fellowship)
          // .extracting("name", "age", "race.name") // alternative
          .extracting(Character::name, Character::age, c -> c.race().name())
          .contains(
              tuple("Frodo", 20, "Hobbit"),
              tuple("Aragorn", 39, "Man"),
              tuple("Legolas", 1000, "Elf")
          );

    }
  }

  @Nested
  @TestMethodOrder(MethodName.class)
  public class Strings {
    private final String string = "abcdef";

    @Test
    public void startsWith_JUnit() {
      assertTrue(string.startsWith("bcd")); // see the failure message
    }

    @Test
    public void startsWith_AssertJ() {
      assertThat(string).startsWith("bcd"); // see the failure message
    }

    @Test
    public void ignoreCase_JUnit() {
      assertEquals("ABCDEF", string.toUpperCase()); // looses the original case
    }

    @Test
    public void ignoreCase_AssertJ() {
      assertThat(string).isEqualToIgnoringCase("AbCdEF");
    }
  }

  @Nested
  @TestMethodOrder(MethodName.class)
  public class Time {
    private final LocalDateTime oneMinAgo = now().minusMinutes(1);

    @Test
    public void maxAge_JUnit() {
      assertTrue(oneMinAgo.isAfter(now().minusSeconds(1)));
    }

    @Test
    public void maxAge_AssertJ() {
      assertThat(oneMinAgo).isCloseTo(now(), byLessThan(1, SECONDS));
    }
  }

  @Nested
  class SoftAssert {
    record Mansion(int guests, String kitchen, String library) {
    }

    interface EventSender {
      void send(String event);
    }

    EventSender eventSender = Mockito.mock(EventSender.class);

    @Test
    void failsOnFirst() {
      Mansion mansion = testedCode();
      assertThat(mansion.guests()).as("Living Guests").isEqualTo(7);
      assertThat(mansion.kitchen()).as("Kitchen").isEqualTo("clean");
      assertThat(mansion.library()).as("Library").isEqualTo("clean");
      Mockito.verify(eventSender).send("mansion-cleaned");
    }

    @Test
    void trySoftly() {
      Mansion mansion = testedCode();

      try (var softly = new AutoCloseableSoftAssertions()) {
        softly.assertThat(mansion.guests()).as("Living Guests").isEqualTo(7);
        softly.assertThat(mansion.kitchen()).as("Kitchen").isEqualTo("clean");
        softly.assertThat(mansion.library()).as("Library").isEqualTo("clean");
        softly.assertThatCode(() -> Mockito.verify(eventSender).send("mansion-cleaned"))
            .as("event published").doesNotThrowAnyException();
      }
    }

    @Nested
    @ExtendWith(SoftlyExtension.class)
    class WithExtension {
      @InjectSoftAssertions
      SoftAssertions softly;

      @Test
      void usingExtension() {
        Mansion mansion = testedCode();

        softly.assertThat(mansion.guests()).as("Living Guests").isEqualTo(7);
        softly.assertThat(mansion.kitchen()).as("Kitchen").isEqualTo("clean");
        softly.assertThat(mansion.library()).as("Library").isEqualTo("clean");
        softly.assertThatCode(() -> Mockito.verify(eventSender).send("mansion-cleaned"))
            .as("event published").doesNotThrowAnyException();
      }

    }

    private Mansion testedCode() {
      return new Mansion(6, "dirty", "clean");
    }
  }

  record Character(String name, int age, Race race) {
  }

  record Race(String name) {
  }
}

