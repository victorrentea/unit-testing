package victor.testing.assertj;


import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
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
      List<Integer> actual = asList(200, 100, 300);

      // check in any order
      assertEquals(asList(100,200,300), actual); // see the failure message
//      assertThat(actual);
   }

}
