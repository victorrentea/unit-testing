package victor.testing.assertions;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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

//      Assert.assertTrue(actual.startsWith("bcd"));
      assertThat(actual).startsWith("bcd");
   }

   @Test
   public void setContent() {
      List<Integer> actual = asList(200, 100, 300);

      // check in any order
//      Assert.assertEquals(asList(100,200,300), actual);
      assertThat(actual).containsExactlyInAnyOrder(100,200,300);
   }

}
