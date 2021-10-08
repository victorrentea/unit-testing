package victor.testing.assertj;


import lombok.Value;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertionsShowcase {

   @Test
   public void listSize() {
      List<Integer> actual = asList(1, 2, 3);

      // I expect to only get one element
      assertEquals(1, actual.size());
//      assertThat(actual);




   }

   @Test
   public void string() {
      String actual = "abcdef";

      assertTrue(actual.startsWith("bcd")); // see the failure message
//      assertThat(actual);
   }






   @Test
   public void setContent() {
      List<Integer> actual = asList(200, 100, 300); // vine fierbinte din productie

      // check in any order
//      assertEquals(new HashSet<>(asList(100, 200, 300)), new HashSet<>(actual)); // see the failure message
      assertThat(actual)
          .containsExactlyInAnyOrder(100, 200, 300)
          .hasSize(3);

//      assertEquals(4, actual.size());

      List<X> actualFromProd = List.of(
          new X(1, "a", "a"),
          new X(10, "a", "a"));

      List<Integer> ids = actualFromProd.stream().map(X::getId).collect(Collectors.toList());
      assertEquals(List.of(1,10), ids);

      assertThat(actualFromProd)
          .map(X::getId)
          .containsExactlyInAnyOrder(10,1);

   }

}


@Value // case class
class X {
   int id;
   String name;
   String address;
}