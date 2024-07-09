package victor.testing.assertj;


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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Disabled("on demand - failures are fun")
public class AssertJ {
  // via dependency: org.assertj:assertj-core
  // or transitive via spring-boot-test

  @Nested
  @TestMethodOrder(MethodName.class)
  public class CollectionsSimple {
    private final List<Integer> list = asList(100, 200, 300, 300);

    @Test
    public void size_JUnit() {
      assertEquals(1, list.size());
    }

    @Test
    public void size_AssertJ() {
      assertThat(list).hasSize(1);
    }

    @Test
    public void onceInAnyOrder_JUnit() {
      assertTrue(list.containsAll(List.of(100, 200, 700)));
      assertEquals(3, list.size());
    }

    @Test
    public void onceInAnyOrder_AssertJ() {
      assertThat(list).containsExactlyInAnyOrder(100, 200, 300);
    }

    @Test
    public void contains_JUnit() {
      assertTrue(list.containsAll(List.of(100, 200)));
      assertFalse(list.contains(500));
    }

    @Test
    public void contains_AssertJ() {
      assertThat(list)
          .contains(100, 200)
          .doesNotContain(500);
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
          .map(Character::getRace)
          .map(Race::getName)
          .collect(toSet());
      assertEquals(Set.of("Man", "Dwarf", "Elf", "Hobbit"), races);
    }

    @Test
    public void attribute_oneOf_AssertJ() {
      assertThat(fellowship)
          .map(Character::getRace)
          .map(Race::getName)
          .containsOnly("Man", "Dwarf", "Elf", "Hobbit");
    }

    @Test
    public void elementsWithProperties_JUnit() {
      assertTrue(fellowship.stream().anyMatch(c -> c.getName().equals("Frodo") && c.getRace().getName().equals("Hobbit")));
      assertTrue(fellowship.stream().anyMatch(c -> c.getName().equals("Aragorn") && c.getRace().getName().equals("Man")));
      assertTrue(fellowship.stream().anyMatch(c -> c.getName().equals("Legolas") && c.getRace().getName().equals("Elf")));
    }

    @Test
    public void elementsWithProperties_AssertJ() {
      assertThat(fellowship)
          // .extracting("name", "age", "race.name") // alternative
          .extracting(Character::getName, Character::getAge, c -> c.getRace().getName())
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
    @Test
    void failsOnFirst() {
      assertThat(1).isEqualTo(2);
      assertThat(3).isEqualTo(4);
    }

    @Test
    void assertAll() {
      try (var softly = new AutoCloseableSoftAssertions()) {
        softly.assertThat(1).isEqualTo(2);
        softly.assertThat(3).isEqualTo(4);
      }
    }
    @Nested
    @ExtendWith(SoftlyExtension.class)
    class WithExtension {
        @InjectSoftAssertions
        SoftAssertions softly;

        @Test
        void assertAll() {
          softly.assertThat(1).isEqualTo(2);
          softly.assertThat(3).isEqualTo(4);
        }

        @Test
        void real() {
          record Mansion(int guests, String kitchen, String library) {
          }
          interface EventSender {
            void send(String event);
          }
          EventSender eventSender = Mockito.mock(EventSender.class);

          // testedCode...
          Mansion mansion = new Mansion(6, "dirty", "clean");

          try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(mansion.guests()).as("Living Guests").isEqualTo(7);
            softly.assertThat(mansion.kitchen()).as("Kitchen").isEqualTo("clean");
            softly.assertThat(mansion.library()).as("Library").isEqualTo("clean");
            softly.assertThatCode(() -> Mockito.verify(eventSender).send("mansion-cleaned"))
                .as("event published").doesNotThrowAnyException();
          }
        }

      }
  }

}

@Value
class Character {
  String name;
  int age;
  Race race;
}

@Value
class Race {
  String name;
}
