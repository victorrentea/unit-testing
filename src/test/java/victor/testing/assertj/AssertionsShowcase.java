package victor.testing.assertj;


import lombok.Value;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertionsShowcase {

   @Nested
   @TestMethodOrder(MethodName.class)
   public class Collections {
      private final List<Integer> listaCu3ElemDinProd = asList(100, 200, 300);
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
      public void size_JUnit() {
         assertThat(listaCu3ElemDinProd.size()).isEqualTo(1);
      }
      @Test
      public void size() {
         assertThat(listaCu3ElemDinProd).hasSize(1);
      }


      // check contents in any order
      @Test
      public void inAnyOrder_JUnit() {
         assertThat(new HashSet<>(listaCu3ElemDinProd)).isEqualTo(Set.of(100, 200, 300));

         assertThat(listaCu3ElemDinProd.contains(500)).isFalse();
      }
      @Test
      public void inAnyOrder() {
         assertThat(listaCu3ElemDinProd)
             .containsExactlyInAnyOrder(100, 200, 300)
//             .doesNotContain(500)
         ;
      }

      @Test
      public void oneAttributeOfElements_JUnit() {
         Set<String> races = fellowship.stream().map(character -> character.getRace().getName()).collect(toSet());
         assertThat(races).isEqualTo(Set.of("Man", "Dwarf", "Elf", "Hobbit"));
      }
      @Test
      public void oneAttributeOfElements() {
         assertThat(fellowship.stream().map(character -> character.getRace().getName()))
             .containsOnly("Man", "Dwarf", "Elf", "Hobbit");
      }

//      assertJ.prototype.functieNoua=
      @Test
      public void subsetAttributesOfElements_JUnit() {
         assertThat(fellowship.stream().anyMatch(c -> c.getName().equals("Frodo") && c.getRace().getName().equals("Hobbit"))).isTrue();
         assertThat(fellowship.stream().anyMatch(c -> c.getName().equals("Aragorn") && c.getRace().getName().equals("Man"))).isTrue();
         assertThat(fellowship.stream().anyMatch(c -> c.getName().equals("Legolas") && c.getRace().getName().equals("Elf"))).isTrue();
      }
      @Test
      public void subsetAttributesOfElements() {
         LocalDateTime now = now();


         assertThat(fellowship).map(Character::getName).contains("Frodo");

         assertThat(fellowship).anyMatch(c -> c.getName().equals("Frodo"));

         assertThat(fellowship).have(new Condition<>(c -> c.getName().equals("Frodxo"), "are Frodo"));

         assertThat(fellowship)
              .extracting("name", "age", "race.name")
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
      private final String actual = "abcdef";

      @Test
      public void stringStarts_JUnit() {
         assertThat(actual.startsWith("bcd")).isTrue(); // see the failure message
      }
      @Test
      public void stringStarts() {
         assertThat(actual).startsWith("bcd"); // see the failure message
      }
      @Test
      public void ignoreCase_JUnit() {
         assertThat(actual.toUpperCase()).isEqualTo("ABCDEF"); // looses the original case
      }
      @Test
      public void ignoreCase() {
         assertThat(actual)
             .isEqualToIgnoringCase("AbCdEF");
      }
   }

   @Nested
   @TestMethodOrder(MethodName.class)
   public class Time {
      private final LocalDateTime oneSecAgo = now().minusSeconds(1);

      @Test
      public void contains() {
         assertThat(oneSecAgo.isAfter(now().minus(1, MINUTES))).isTrue();
      }
      @Test
      public void stringStarts() {
         assertThat(oneSecAgo).isCloseTo(now(), byLessThan(1, MINUTES));
      }
   }

}

@Value
class Character {
   String name;
   int age;
   Race race;
}

@Value class Race {String name;}
